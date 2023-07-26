package com.sunhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunhy.common.R;
import com.sunhy.entity.Category;
import com.sunhy.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
