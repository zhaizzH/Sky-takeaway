package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
     * 用户分页条件查询用户订单历史
     * @param ordersPageQueryDTO 查询参数实体类
     * @return 订单历史列表
     */
    @Select("select * from orders where user_id = #{userId} order by order_time desc")
    Page<Orders> pageQueryUser(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单id查询订单
     * @param id 订单id
     * @return 订单数据
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 管理员分页条件查询订单列表
     * @param ordersPageQueryDTO 查询参数实体类
     * @return 订单列表
     */
    Page<Orders> pageQueryAdmin(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计订单状态数量
     * @param toBeConfirmed 订单状态
     * @return 订单状态数量
     */
    Integer countByStatus(Integer toBeConfirmed);

    /**
     * 查询超时订单
     * @return 超时订单列表
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 营业额统计
     * @param map 查询参数
     * @return 营业额
     */
    Double countByMap(Map<String,Object> map);

    /**
     * 销售排名统计
     * @param begin 开始时间
     * @param end   结束时间
     * @return 销售排名统计结果
     */
    List<GoodsSalesDTO> getSalesTop(LocalDateTime begin, LocalDateTime end);

    /**
     * 订单金额统计
     * @param map 查询参数
     * @return 订单金额
     */
    Double sumByMap(Map<String, Object> map);
}
