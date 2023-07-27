package com.sunhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunhy.common.R;
import com.sunhy.entity.Category;
import com.sunhy.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;


    //新增分类
    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        log.info("新增分类：{}",category);
        categoryService.save(category);
        return R.success("新增分类成功^_^");
    }

    //分页查询
    @GetMapping("/page")
    public R<Page<Category>> getPage(Integer page,Integer pageSize){
        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        //添加排序
        wrapper.orderByAsc(Category::getSort);
        categoryService.page(categoryPage,wrapper); //分页查询

        return R.success(categoryPage);
    }

    //删除分类
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类：{}",ids);
        categoryService.remove(ids);

//        categoryService.removeById(id);
        return R.success("删除分类成功^_^");
    }

    //修改分类
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类：{}",category);
        //这里已经自动填充了updateTime和updateUser
        categoryService.updateById(category);
        return R.success("修改分类成功^_^");
    }

    //根据条件查询分类数据,填充下拉框的数据
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType()!=null,Category::getType,category.getType());
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }
}
