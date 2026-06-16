package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId 菜品分类ID
     * @return 该分类下菜品总条数
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     * @param dish 菜品实体类
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO 分页查询菜品DTO
     * @return 分页查询结果VO列表
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     * @param id 菜品ID
     * @return 菜品实体类
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据菜品id集合批量删除菜品
     * @param ids 菜品ID集合
     */
    void deleteByIds(List<Long> ids);

    /**
     * 更新菜品信息基本信息
     * @param dish 菜品实体类
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类id
     * @return 菜品列表
     */
    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> getByCategoryId(Integer categoryId);

    /**
     * 条件查询菜品
     * @param dish 查询参数实体类
     * @return 菜品列表
     */
    @Select("select * from dish where category_id=#{categoryId} and status=#{status}")
       List<Dish> list(Dish dish);
}
