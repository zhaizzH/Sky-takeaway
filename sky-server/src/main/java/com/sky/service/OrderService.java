package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
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
    void cancelOrderUser(Long id);

    /**
     * 再来一单接口
     * @param id 订单id
     */
    void repetitionOrder(Long id);

    /**
     * 订单搜索接口
     * @param ordersPageQueryDTO 订单搜索参数
     * @return 订单搜索结果
     */
    PageResult pageQueryAdmin(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单统计接口
     * @return 订单统计结果
     */
    OrderStatisticsVO getOrderStatistics();

    /**
     * 订单详情接口
     * @param id 订单id
     * @return 订单详情
     */
    OrderVO getById(Long id);

    /**
     * 接单接口
     * @param ordersConfirmDTO 订单确认实体类
     */
    void confirmOrder(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单接口
     * @param ordersRejectionDTO 订单拒绝实体类
     */
    void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 取消订单接口
     * @param ordersCancelDTO 订单取消实体类
     */
    void cancelOrderAdmin(OrdersCancelDTO ordersCancelDTO);

    /**
     * 配送订单接口
     * @param id 订单id
     */
    void deliveryOrder(Long id);

    /**
     * 完成订单接口
     * @param id 订单id
     */
    void completeOrder(Long id);
}
