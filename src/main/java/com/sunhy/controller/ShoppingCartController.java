package com.sunhy.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sunhy.common.BaseContext;
import com.sunhy.common.R;
import com.sunhy.entity.ShoppingCart;
import com.sunhy.service.IShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    //添加购物车
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("添加购物车，{}", shoppingCart);
        //设置用户ID
        Long currentID = BaseContext.getCurrentID();
        shoppingCart.setUserId(currentID);

        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,currentID);
        if (dishId != null) {
            //菜品
            wrapper.eq(ShoppingCart::getDishId,dishId);

        } else {
            //套餐
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart one = shoppingCartService.getOne(wrapper);

        if (one != null) {
            //更新数量
            Integer number = one.getNumber();
            one.setNumber(number+1);
//            one.setNumber(number+shoppingCart.getNumber());
            shoppingCartService.updateById(one);
        }else {
            //添加
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one=shoppingCart;
        }

        return R.success(one);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        Long userID = BaseContext.getCurrentID();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userID)
                .orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(wrapper);

        return R.success(list);

    }

    //清空购物车
    @DeleteMapping("/clean")
    public R<String> clean(){
        Long userID = BaseContext.getCurrentID();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userID);
        shoppingCartService.remove(wrapper);
        return R.success("清空成功");
    }
}
