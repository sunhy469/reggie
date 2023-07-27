package com.sunhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunhy.common.CustomException;
import com.sunhy.entity.Category;
import com.sunhy.entity.Dish;
import com.sunhy.entity.Setmeal;
import com.sunhy.mapper.CategoryMapper;
import com.sunhy.service.ICategoryService;
import com.sunhy.service.IDIshService;
import com.sunhy.service.ISetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService  {

    @Autowired
    private IDIshService dishService;

    @Autowired
    private ISetmealService setmealService;

    //根据id删除，删除之前需要判断当前分类是否被套餐使用了
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联到菜品
        LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
        dishWrapper.eq(Dish::getCategoryId,id);
        int count = (int) dishService.count(dishWrapper);
        if (count>0){
            //当前分类被菜品使用了,抛出异常
            throw new CustomException("当前分类被菜品使用了，不能删除");
        }

        //查询当前分类是否关联到套餐
        LambdaQueryWrapper<Setmeal> setmealWWrapper = new LambdaQueryWrapper<>();
        setmealWWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = (int) setmealService.count(setmealWWrapper);
        if (count2>0){
            //当前分类被套餐使用了,抛出异常
            throw new CustomException("当前分类被套餐使用了，不能删除");
        }

        //正常删除
        super.removeById(id);
    }
}
