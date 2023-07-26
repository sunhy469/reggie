package com.sunhy.controller;

import com.sunhy.common.R;
import com.sunhy.entity.Category;
import com.sunhy.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
