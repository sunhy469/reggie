package com.sunhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunhy.dto.DishDto;
import com.sunhy.entity.Dish;
import com.sunhy.entity.DishFlavor;
import com.sunhy.mapper.DishMapper;
import com.sunhy.service.IDIshService;
import com.sunhy.service.IDishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.util.Arrays;
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
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishID);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    //查询菜品，同时查询对应得到口味
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = this.getById(id);

        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(wrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    //更新菜品信息，同时更新口味
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        Long dishID = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishID);
        dishFlavorService.remove(wrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        List<DishFlavor> newFavors = flavors.stream().map((item) -> {
            item.setDishId(dishID);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(newFavors);
    }

    ////逻辑删除菜品信息
    @Override
    public void deleteById(Long[] ids) {
        Arrays.stream(ids).forEach(id -> {
            this.removeById(id);

            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(wrapper);
        });

    }

    @Override
    public void updateStatus(int status, Long[] ids) {
        Arrays.stream(ids).forEach(id -> {
            Dish dish = this.getById(id);
            //这个判断很奇妙，在表中status为1才是启用状态，但是客户端发请求修改状态时恰好相反
            dish.setStatus(status == 0 ? 0 : 1);
            this.updateById(dish);
        });
    }
}
