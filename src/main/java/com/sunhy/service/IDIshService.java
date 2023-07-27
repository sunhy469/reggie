package com.sunhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunhy.dto.DishDto;
import com.sunhy.entity.Dish;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
public interface IDIshService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据 dish 和 dish_flavor表
    public void saveWithFlavor(DishDto dishDto);
}
