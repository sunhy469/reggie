package com.sunhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunhy.common.R;
import com.sunhy.dto.SetmealDto;
import com.sunhy.entity.Category;
import com.sunhy.entity.Setmeal;
import com.sunhy.service.ICategoryService;
import com.sunhy.service.ISetmealDishService;
import com.sunhy.service.ISetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description: 套餐管理
 * @Version 1.0
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private ISetmealDishService setmealDishService;

    @Autowired
    private ISetmealService setmealService;

    @Autowired
    private ICategoryService categoryService;

    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)//清除缓存
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息"+setmealDto);

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功^_^");
    }

    //套餐分页查询
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(Integer page, Integer pageSize, String name){
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null,Setmeal::getName,name)
                .orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage,wrapper);

        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list=null;
        list=records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    //删除套餐
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)//清除缓存
    public R<String> delete(@RequestParam(name = "ids") List<Long> ids){
        log.info("删除套餐数据"+ids);

        setmealService.removeWithDish(ids);

        return R.success("删除套餐成功^_^");
    }

    //状态修改
    @PostMapping("/status/{status}")
    public R<String> statusUpdate(@PathVariable Integer status,@RequestParam(name = "ids") List<Long> ids){
        log.info("修改套餐状态"+status+" "+ids);

        setmealService.updateStatus(status,ids);

        return R.success("修改套餐状态成功^_^");
    }

    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")//缓存数据
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        wrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(wrapper);

        return R.success(list);
    }
}
