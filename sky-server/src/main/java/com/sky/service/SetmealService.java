package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 分页查询参数
     * @return 分页查询结果
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmealDTO 套餐DTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 根据套餐id集合批量删除套餐
     * @param ids 套餐ID集合
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id查询套餐
     * @param id 套餐ID
     * @return 套餐DTO
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐
     * @param setmealDTO 套餐DTO
     */
    void updateWithDish(SetmealDTO setmealDTO);

    /**
     * 套餐起售/停售
     * @param id 套餐ID
     * @param status 状态 0:停用 1:启用
     */
    void startOrStop(Long id, Integer status);
}
