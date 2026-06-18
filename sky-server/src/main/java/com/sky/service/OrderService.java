package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户点单接口
     * @param ordersSubmitDTO 用户点单参数
     * @return 订单提交结果
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付接口
     * @param ordersPaymentDTO 订单支付参数
     * @return 订单支付结果
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo 订单号
     */
    void paySuccess(String outTradeNo);

    /**
     * 查询用户订单历史接口
     * @param ordersPageQueryDTO 查询参数
     * @return 订单历史列表
     */
    PageResult pageQueryUser(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询订单详情接口
     * @param id 订单id
     * @return 订单详情
     */
    OrderVO getOrderDetail(Long id);

    /**
     * 取消订单接口
     * @param id 订单id
     */
    void cancelOrder(Long id);

    /**
     * 再来一单接口
     * @param id 订单id
     */
    void repetitionOrder(Long id);
}
