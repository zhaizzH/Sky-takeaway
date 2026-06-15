package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品和口味
     * @param dishDTO 新增参数
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO 分页查询参数
     * @return 分页查询结果
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids 菜品id列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据菜品id查询菜品详情
     * @param id 菜品id
     * @return 菜品详情
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 更新菜品和口味
     * @param dishDTO 更新参数
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 启用/停用菜品
     * @param id 菜品id
     * @param status 状态 0：停用 1：启用
     */
    void startOrStop(Long id, Integer status);

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类id
     * @return 菜品列表
     */
    List<Dish> listByCategoryId(Integer categoryId);
}
