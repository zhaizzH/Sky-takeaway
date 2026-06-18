package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api("管理员订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索接口
     * @param ordersPageQueryDTO 订单搜索参数
     * @return 订单搜索结果
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单搜索条件：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.pageQueryAdmin(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 订单统计接口
     * @return 订单统计结果
     */
    @GetMapping("/statistics")
    @ApiOperation("订单统计")
    public Result<OrderStatisticsVO> statistics(){
        log.info("订单统计");
        OrderStatisticsVO orderStatisticsVO = orderService.getOrderStatistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 订单详情接口
     * @param id 订单id
     * @return 订单详情
     */
    @GetMapping("/details/{id}")
    @ApiOperation("订单详情")
    public Result<OrderVO> getById(@PathVariable Long id){
        log.info("根据订单id查询订单详情：{}", id);
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    /**
     * 接单
     * @param ordersConfirmDTO 订单确认实体类
     * @return 确认结果
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result<Void> confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单：{}", ordersConfirmDTO);
        orderService.confirmOrder(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 拒单
     * @param ordersRejectionDTO 订单拒绝实体类
     * @return 拒绝结果
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result<Void> rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("拒单：{}", ordersRejectionDTO);
        orderService.rejectionOrder(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 取消订单
     * @param ordersCancelDTO 订单取消实体类
     * @return 取消结果
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result<Void> cancel(@RequestBody OrdersCancelDTO ordersCancelDTO){
        log.info("取消订单：{}", ordersCancelDTO);
        orderService.cancelOrderAdmin(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 派送订单
     * @param id 订单id
     * @return 配送结果
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("配送订单")
    public Result<Void> delivery(@PathVariable Long id){
        log.info("配送订单：{}", id);
        orderService.deliveryOrder(id);
        return Result.success();
    }

    /**
     * 完成订单
     * @param id 订单id
     * @return 完成结果
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result<Void> completeOrder(@PathVariable Long id){
        log.info("完成订单：{}", id);
        orderService.completeOrder(id);
        return Result.success();
    }
}
