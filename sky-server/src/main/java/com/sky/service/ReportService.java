package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * 销售额统计
     * @param begin 开始时间
     * @param end 结束时间
     * @return 销售额统计结果
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     * @param begin 开始时间
     * @param end 结束时间
     * @return 用户统计结果
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);
}
