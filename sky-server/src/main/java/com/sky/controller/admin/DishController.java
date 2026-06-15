package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    /**
     * 新增菜品
     * @param dishDTO 新增参数
     * @return 操作结果
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result<Void> save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO 分页查询参数DTO
     * @return 分页查询结果VO列表
     */
    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> page(DishPageQueryDTO  dishPageQueryDTO) {
        log.info("分页查询菜品：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除
     * @param ids 菜品id列表
     * @return 操作结果
     */
    @DeleteMapping
    @ApiOperation("菜品单个/批量删除")
    public Result<Void> delete(@RequestParam List<Long> ids) {
        log.info("菜品单个/批量删除：{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据菜品id查询菜品详情
     * @param id 菜品id
     * @return 菜品详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据菜品id查询菜品详情")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据菜品id查询菜品详情：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 更新菜品
     * @param dishDTO 更新参数
     * @return 操作结果
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result<Void> update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 启用/停用菜品
     * @param status 状态 0：停用 1：启用
     * @param id 菜品id
     * @return 操作结果
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用/停用菜品")
    public Result<Void> startOrStop(@PathVariable Integer status,Long id){
        log.info("启用/停用菜品：{}", status);
        dishService.startOrStop(id,status);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类id
     * @return 菜品列表
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Integer categoryId){
        log.info("根据分类id查询菜品：{}", categoryId);
        List<Dish> list = dishService.listByCategoryId(categoryId);
        return Result.success(list);
    }
}
