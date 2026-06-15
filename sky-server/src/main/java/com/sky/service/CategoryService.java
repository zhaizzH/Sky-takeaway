package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import java.util.List;

public interface CategoryService {

    /**
     * 新增分类
     * @param categoryDTO 新增参数
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分页查询
     * @param categoryPageQueryDTO 分页查询参数
     * @return 分页查询结果
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据id删除分类
     * @param id 分类id
     */
    void deleteById(Long id);

    /**
     * 修改分类
     * @param categoryDTO 修改参数
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 启用、禁用分类
     * @param status 状态 1 启用 2 禁用
     * @param id 分类id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据类型查询分类
     * @param type 分类类型 1 配送分类 2 餐品分类
     * @return 分类列表
     */
    List<Category> list(Integer type);
}
