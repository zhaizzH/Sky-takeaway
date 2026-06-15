package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 分页查询参数
     * @return 分页查询结果
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        long total;
        List<Setmeal> records;
        try (Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO)) {

            total = page.getTotal();
            records = page.getResult();
        }
        return new PageResult(total,records);
    }

    /**
     * 新增套餐
     * @param setmealDTO 套餐DTO
     */
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();

        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 向套餐表插入数据
        setmealMapper.insert(setmeal);

        // 获取insert语句生成的主键值
        Long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(dish -> {
                dish.setSetmealId(setmealId);
            });
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 套餐批量删除
     * @param ids 套餐ID集合
     */
    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        // 判断当前套餐是否能够被删除--是否为起售套餐
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == 1) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        // 根据套餐id集合批量删除套餐
        setmealMapper.deleteByIds(ids);

        // 根据套餐id集合批量删除套餐中的菜品
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    /**
     * 根据id查询套餐
     * @param id 套餐ID
     * @return 套餐DTO
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        Setmeal setmeal = setmealDishMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getSetmealDishesBySetmealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO 套餐DTO
     */
    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();

        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 修改套餐基本信息
        setmealMapper.update(setmeal);

        // 删除原有套餐中的菜品
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        // 套餐中新增菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(dish -> {
                dish.setSetmealId(setmealDTO.getId());
            });
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 套餐起售/停售
     * @param id 套餐ID
     * @param status 状态 0:停用 1:启用
     */
    @Override
    public void startOrStop(Long id, Integer status) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
}
