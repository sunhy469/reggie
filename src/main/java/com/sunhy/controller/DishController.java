package com.sunhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunhy.common.R;
import com.sunhy.dto.DishDto;
import com.sunhy.entity.Category;
import com.sunhy.entity.Dish;
import com.sunhy.service.ICategoryService;
import com.sunhy.service.IDIshService;
import com.sunhy.service.IDishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description: 菜品控制器
 * @Version 1.0
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private IDIshService dishService;

    @Autowired
    private IDishFlavorService dishFlavorService;

    @Autowired
    private ICategoryService categoryService;

    //新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("新增菜品数据"+dishDto);
        //操作两张表
        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功^_^");
    }

    //菜品信息分页查询
    @GetMapping("/page")
    public R<Page<DishDto>> page(Integer page, Integer pageSize, String name){
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null,Dish::getName,name);
        wrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,wrapper);

        //拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    //回显菜品数据
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    //修改菜品
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info("修改菜品数据"+dishDto);
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功^_^");
    }

//    删除菜品
    @DeleteMapping
    public R<String> delete(Long[] ids){

        dishService.deleteById(ids);

        return R.success("删除成功^_^");
    }

    //状态修改
    @PostMapping("/status/{status}")
    public R<String> statusUpdate(@PathVariable int status, Long[] ids){
        log.info("修改菜品状态"+status+" "+ Arrays.toString(ids));

        dishService.updateStatus(status,ids);

        return R.success("修改成功");
    }
}
