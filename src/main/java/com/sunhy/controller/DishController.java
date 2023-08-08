package com.sunhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunhy.common.R;
import com.sunhy.dto.DishDto;
import com.sunhy.entity.Category;
import com.sunhy.entity.Dish;
import com.sunhy.entity.DishFlavor;
import com.sunhy.service.ICategoryService;
import com.sunhy.service.IDIshService;
import com.sunhy.service.IDishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private RedisTemplate redisTemplate;


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
    public R<String> delete(@RequestParam(name = "ids") Long[] ids){

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

    //回显到套餐管理的菜品菜单
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList=null;
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        //从redis获取缓存数据
        Object object=(Object)redisTemplate.opsForValue().get(key);
        dishDtoList=(List<DishDto>)object;
        if (dishDtoList!=null){
            return R.success(dishDtoList);
        }



        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId())
                .eq(dish.getStatus()!=null,Dish::getStatus,dish.getStatus());
        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(wrapper);

        dishDtoList=list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            Long dishID = item.getId();
            LambdaQueryWrapper<DishFlavor> flavorsWrapper = new LambdaQueryWrapper<>();
            flavorsWrapper.eq(DishFlavor::getDishId,dishID);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(flavorsWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        //缓存
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId())
//                .eq(dish.getStatus()!=null,Dish::getStatus,dish.getStatus());
//        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(wrapper);
//        return R.success(list);
//    }
}
