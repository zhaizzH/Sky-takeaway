package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;

    /**
     * 用户点单接口
     *
     * @param ordersSubmitDTO 用户点单参数
     * @return 订单提交结果
     */
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //处理各种业务异常(地址簿为空、购物车数据为空)
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            //抛出业务异常
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 查询当前用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingcart = new ShoppingCart();
        shoppingcart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingcart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            //抛出业务异常
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //向订单表插入1条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);
        orders.setAddress(addressBook.getProvinceName()+addressBook.getCityName()+addressBook.getDetail());

        orderMapper.insert(orders);

        //向订单明细表插入n条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();//订单明细
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());//设置当前订单明细关联的订单id
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        //清空当前用户的购物车数据
        ShoppingCart deleteShoppingCart = new ShoppingCart();
        deleteShoppingCart.setUserId(userId);
        shoppingCartMapper.deleteByUserId(deleteShoppingCart);

        //封装vo返回结果
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    /**
     * 订单支付
     * @param ordersPaymentDTO 订单支付参数
     * @return 订单支付结果
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal("0.01"), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo 订单号
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 分页条件查询用户订单历史
     * @param ordersPageQueryDTO 查询参数
     * @return 订单历史列表
     */
    @Override
    public PageResult pageQueryUser(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 设置分页
       PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

       ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());

        // 分页条件查询
        try (Page<Orders> page = orderMapper.pageQueryUser(ordersPageQueryDTO)) {

            List<OrderVO> list = new ArrayList<>();

            if (page != null && !page.isEmpty()) {
                for (Orders orders : page) {
                    Long orderId = orders.getId();// 订单id

                    // 查询订单明细
                    List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                    OrderVO orderVO = new OrderVO();
                    BeanUtils.copyProperties(orders, orderVO);
                    orderVO.setOrderDetailList(orderDetails);

                    list.add(orderVO);
                }
            }

            return new PageResult(page.getTotal(), list);
        }
    }

    /**
     * 查询订单详情接口
     * @param id 订单id
     * @return 订单详情
     */
    @Override
    public OrderVO getOrderDetail(Long id) {
        // 根据订单id查询订单
        Orders orders = orderMapper.getById(id);

        // 查询订单明细
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);

        // 封装vo返回结果
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);

        return orderVO;
    }

    /**
     * 取消订单
     * @param id 订单id
     */
    @Override
    public void cancelOrderUser(Long id) {
        // 根据id修改订单状态
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.CANCELLED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 再来一单
     * @param id 订单id
     */
    @Override
    public void repetitionOrder(Long id) {
        // 获取id对应的订单信息
        Orders orders = orderMapper.getById(id);

        //向订单表插入修改后的1条数据
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));

        orderMapper.insert(orders);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        // 根据id查询orderId，获取再来一单中所有商品明细
        List<OrderDetail> historicalOrders =orderDetailMapper.getByOrderId(id);

        for (OrderDetail list : historicalOrders) {
            OrderDetail orderDetail = new OrderDetail();//订单明细
            BeanUtils.copyProperties(list, orderDetail);
            orderDetail.setOrderId(orders.getId());//设置当前订单明细关联的订单id
            orderDetailList.add(orderDetail);
        }

        //向订单明细表插入n条数据
        orderDetailMapper.insertBatch(orderDetailList);
    }

    @Override
    public PageResult pageQueryAdmin(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        try (Page<Orders> page = orderMapper.pageQueryAdmin(ordersPageQueryDTO)) {
            return new PageResult(page.getTotal(), page.getResult());
        }
    }

    /**
     * 订单统计接口
     * @return 订单统计结果
     */
    @Override
    public OrderStatisticsVO getOrderStatistics() {
        // 统计： 2待接单 3已接单 4派送中
        // 统计待接单数量
        Integer toBeConfirmed = orderMapper.countByStatus(Orders.TO_BE_CONFIRMED);

        // 统计已接单数量
        Integer confirmed = orderMapper.countByStatus(Orders.CONFIRMED);

        // 统计派送中数量
        Integer deliveryInProgress = orderMapper.countByStatus(Orders.DELIVERY_IN_PROGRESS );

        // 封装vo返回结果
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);

        return orderStatisticsVO;
    }

    /**
     * 订单详情接口
     * @param id 订单id
     * @return 订单详情
     */
    @Override
    public OrderVO getById(Long id) {
        // 根据订单id查询订单
        Orders orders = orderMapper.getById(id);

        // 查询订单明细
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);

        // 封装vo返回结果
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);

        return orderVO;
    }

    /**
     * 接单接口
     * @param ordersConfirmDTO 订单确认实体类
     */
    @Override
    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO) {
        // 根据id修改订单状态
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 拒单接口
     * @param ordersRejectionDTO 订单拒绝实体类
     */
    @Override
    public void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO) {
        // 根据id修改订单状态
        Orders orders = Orders.builder()
                .id(ordersRejectionDTO.getId())
                .status(Orders.CANCELLED)
                .rejectionReason(ordersRejectionDTO.getRejectionReason())
                .build();
        orderMapper.update(orders);
    }

    /**
     * 取消订单接口
     * @param ordersCancelDTO 订单取消实体类
     */
    @Override
    public void cancelOrderAdmin(OrdersCancelDTO ordersCancelDTO) {
        // 根据id修改订单状态
        Orders orders = Orders.builder()
                .id(ordersCancelDTO.getId())
                .status(Orders.CANCELLED)
                .cancelReason(ordersCancelDTO.getCancelReason())
                .cancelTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);
    }

    /**
     * 配送订单接口
     * @param id 订单id
     */
    @Override
    public void deliveryOrder(Long id) {
        // 根据id修改订单状态
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.DELIVERY_IN_PROGRESS)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 完成订单接口
     *
     * @param id 订单id
     */
    @Override
    public void completeOrder(Long id) {
        // 根据id修改订单状态
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.COMPLETED)
                .build();
        orderMapper.update(orders);
    }
}
