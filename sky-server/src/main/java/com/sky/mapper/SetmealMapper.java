package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param categoryId 分类id
     * @return 套餐数量
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO 分页查询套餐DTO
     * @return 分页查询结果列表
     */
    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 修改套餐
     *
     * @param setmeal 套餐详情
     */
    void update(Setmeal setmeal);

    /**
     * 根据套餐id集合批量删除套餐中的菜品
     *
     * @param ids 套餐id列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 新增套餐
     *
     * @param setmeal 套餐
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 根据套餐id查询套餐详情
     * @param id 套餐id
     * @return 套餐详情
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);
}
