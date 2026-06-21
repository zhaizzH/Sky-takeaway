package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api("用户订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单接口
     *
     * @param ordersSubmitDTO 用户下单参数
     * @return 订单提交结果
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单参数: {}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付接口
     *
     * @param ordersPaymentDTO 订单支付参数
     * @return 订单支付结果
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
//        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
//        log.info("生成预支付交易单：{}", orderPaymentVO);
        //调用支付成功接口，原接口为WX台付数成功后的接口回调、目的保证在成功之后再修改数据，现无wx后台访问，故直接调用paySuccess
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success();
    }

    /**
     * 查询用户订单历史接口
     *
     * @param ordersPageQueryDTO 查询参数
     * @return 订单历史列表
     */
    @GetMapping("/historyOrders")
    @ApiOperation("查询用户订单历史")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("查询用户订单历史参数: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.pageQueryUser(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详情接口
     *
     * @param id 订单id
     * @return 订单详情
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> orderDetail(@PathVariable Long id) {
        log.info("查询订单详情参数: {}", id);
        OrderVO orderVO = orderService.getOrderDetail(id);
        return Result.success(orderVO);
    }

    /**
     * 取消订单接口
     * @param id 订单id
     * @return 取消订单结果
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result<Void> cancel(@PathVariable Long id) {
        log.info("取消订单参数: {}", id);
        orderService.cancelOrderUser(id);
        return Result.success();
    }

    /**
     * 再来一单接口
     * @param id 订单id
     * @return 再来一单结果
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    @Transactional
    public Result<Void> repetition(@PathVariable Long id) {
        log.info("再来一单参数: {}", id);
        orderService.repetitionOrder(id);
        return Result.success();
    }

    /**
     * 催单接口
     * @param id 订单id
     * @return 催单结果
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("催单接口")
    public Result<Void> reminder(@PathVariable Long id) {
        log.info("催单参数: {}", id);
        orderService.reminderOrder(id);
        return Result.success();
    }
}
