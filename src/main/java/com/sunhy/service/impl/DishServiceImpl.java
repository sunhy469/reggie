package com.sunhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunhy.dto.DishDto;
import com.sunhy.entity.Dish;
import com.sunhy.entity.DishFlavor;
import com.sunhy.mapper.DishMapper;
import com.sunhy.service.IDIshService;
import com.sunhy.service.IDishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDIshService {

    @Autowired
    private IDishFlavorService dishFlavorService;

    //新增菜品，同时插入菜品对应的口味数据 dish 和 dish_flavor表

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到dish
        this.save(dishDto);
        //保存口味
        Long dishID = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishID);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
