package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param orders 订单数据
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber 订单号
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders 订单数据
     */
    void update(Orders orders);

    /**
     * 分页条件查询用户订单历史
     * @param ordersPageQueryDTO 查询参数实体类
     * @return 订单历史列表
     */
    @Select("select * from orders where user_id = #{userId} order by order_time desc")
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单id查询订单
     * @param id 订单id
     * @return 订单数据
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);
}
