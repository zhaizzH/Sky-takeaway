package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Api(tags = "订单数据统计相关接口")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 销售额统计结果
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("获取营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("获取营业额统计，开始时间：{}，结束时间：{}", begin, end);
        return Result.success(reportService.getTurnoverStatistics(begin, end));
    }

    /**
     * 用户统计
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 用户统计结果
     */
    @GetMapping("/userStatistics")
    @ApiOperation("获取用户数据统计")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("获取用户数据统计，开始时间：{}，结束时间：{}", begin, end);
        return Result.success(reportService.getUserStatistics(begin, end));
    }

    public
}
