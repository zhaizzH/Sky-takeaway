package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 设置日期列表
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 处理后日期列表
     */
    private List<LocalDate> setLastDate(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();

        if (begin.equals(end)) {
            dateList.add(begin);
        } else {
            // 处理多天订单统计
            do {
                dateList.add(begin);
                begin = begin.plusDays(1);
            } while (!begin.equals(end));
        }
        return dateList;
    }


    /**
     * 销售额统计
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 销售额统计结果
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = setLastDate(begin, end); // 处理日期范围

        List<Double> turnoverList = new ArrayList<>(); // 单日营业额
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.countTurnoverByMap(map);
            // 处理空值
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 用户统计
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 用户统计结果
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = setLastDate(begin, end); // 处理日期范围

        List<Long> totalUserList = new ArrayList<>();
        List<Long> newUserList = new ArrayList<>();

        Long totalUser=0L;
        for (LocalDate date:dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);

            Long newUser=userMapper.countNewUserByMap(map);

            // 处理空值
            newUser = newUser == null ? 0L : newUser;
            totalUser+=newUser;

            newUserList.add(newUser);
            totalUserList.add(totalUser);

        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }
}

