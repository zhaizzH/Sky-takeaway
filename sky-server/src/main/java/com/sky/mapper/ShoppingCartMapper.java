package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 动态条件查询购物车列表
     * @param shoppingCart 购物车实体
     * @return 购物车列表
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新购物车商品数量
     * @param shoppingCart 购物车实体
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumber(ShoppingCart shoppingCart);

    /**
     * 添加购物车商品
     * @param shoppingCart 购物车实体
     */
    @Insert("insert into shopping_cart (user_id, dish_id, setmeal_id, number, amount, image, name, create_time,dish_flavor) " +
            "values (#{userId}, #{dishId}, #{setmealId}, #{number}, #{amount}, #{image}, #{name}, #{createTime},#{dishFlavor})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * @param shoppingCart 购物车实体
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(ShoppingCart shoppingCart);
}
