package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO 分页查询参数DTO
     * @return 分页查询结果VO列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询：{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐DTO
     * @return 操作结果
     */
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache", key="#setmealDTO.categoryId")
    public Result<Void> saveWithDish(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐删除
     *
     * @param ids 套餐ID集合
     * @return 操作结果
     */
    @DeleteMapping
    @ApiOperation("套餐单个/批量删除")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<Void> delete(@RequestParam List<Long> ids) {
        log.info("套餐单个/批量删除：{}", ids);
        setmealService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 根据id查询套餐
     *
     * @param id 套餐ID
     * @return 套餐DTO
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据id查询套餐：{}", id);
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO 套餐DTO
     * @return 操作结果
     */
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<Void> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);
        setmealService.updateWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐起售/停售
     *
     * @param id     套餐ID
     * @param status 状态
     * @return 操作结果
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售/停售")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<Void> startOrStop(@PathVariable Integer status, Long id) {
        log.info("套餐起售/停售：{} {}", status, id);
        setmealService.startOrStop(id, status);
        return Result.success();
    }
}
