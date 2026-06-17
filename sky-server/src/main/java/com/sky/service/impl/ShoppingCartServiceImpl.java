package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO 购物车数据DTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 判断当前商品是否再购物车中存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        // 存在，只需要将商品数量加一
        if (list != null && !list.isEmpty()) {
            shoppingCart = list.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateNumber(shoppingCart);
        } else {
            // 不存在，需要新增商品到购物车

            // 判断本次添加到购物车的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                // 菜品
                Dish dish = dishMapper.getById(dishId);

                shoppingCart.setDishId(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 套餐
                Long setmealId = shoppingCart.getSetmealId();

                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setSetmealId(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }

    }

    /**
     * 查询购物车列表
     *
     * @return 购物车列表
     */
    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(userId).build();
        return shoppingCartMapper.list(shoppingCart);
    }


    @Override
    public void cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart=ShoppingCart.builder().userId(userId).build();

        shoppingCartMapper.deleteByUserId(shoppingCart);
    }

    /**
     * 删除购物车中一个商品数量
     * @param shoppingCartDTO 购物车数据DTO
     */
    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);

        // 查询当前商品数量
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        ShoppingCart cart = list.get(0);

        // 判断当前商品数量是否为1
        if (cart.getNumber() == 1) {
            // 如果为1，直接删除商品
            shoppingCartMapper.deleteById(cart);
        } else {
            // 如果不为1，将商品数量减1
            cart.setNumber(cart.getNumber() - 1);
            shoppingCartMapper.updateNumber(cart);
        }
    }
}
