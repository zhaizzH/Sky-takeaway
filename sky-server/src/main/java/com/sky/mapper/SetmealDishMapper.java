package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询对应的套餐id
     *
     * @param dishIds 菜品id列表
     * @return 套餐id列表
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 新增套餐中的菜品
     *
     * @param setmealDishes 套餐中的菜品列表
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询套餐详情
     *
     * @param id 套餐id
     * @return 套餐详情
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据套餐id集合批量删除套餐中的菜品
     *
     * @param ids 套餐id列表
     */
    void deleteBySetmealIds(List<Long> ids);

    /**
     * 根据套餐id查询套餐中的菜品
     *
     * @param id 套餐id
     * @return 套餐中的菜品列表
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getSetmealDishesBySetmealId(Long id);

    /**
     * 根据套餐id删除套餐中的菜品
     *
     * @param dishId 套餐id
     */
    @Delete("delete from setmeal_dish where dish_id = #{dishId}")
    void deleteBySetmealId(Long dishId);
}
