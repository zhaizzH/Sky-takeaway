package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "购物车相关接口")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 添加购物车
     * @param shoppingCartDTO 购物车数据DTO
     * @return 添加结果
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result<Void> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车,商品数据为：{}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查询购物车商品列表
     * @return 购物车商品列表
     */
    @GetMapping("/list")
    @ApiOperation("查询购物车列表")
    public Result<List<ShoppingCart>> list() {
        log.info("查询购物车列表");
        List<ShoppingCart> list = shoppingCartService.list();
        return Result.success(list);
    }

    /**
     * 清空购物车
     * @return 清空结果
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result<Void> clean() {
        log.info("清空购物车");
        shoppingCartService.cleanShoppingCart();

        // 批量删除dish_开头的key
        cleanCache("dish*");
        return Result.success();
    }

    /**
     * 删除购物车中一个商品数量
     * @param shoppingCartDTO 购物车数据DTO
     * @return 删除结果
     */
    @PostMapping("/sub")
    @ApiOperation("删除购物车中一个商品数量")
    public Result<Void> sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车中一个商品数量,商品数据为：{}", shoppingCartDTO);
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 清理分类id对应的redis缓存
     * @param pattern key名称模式
     */
    private void cleanCache(String pattern){
        Set<Object> keys=redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
