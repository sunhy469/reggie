package com.sunhy.controller;

import com.sunhy.common.R;
import com.sunhy.dto.DishDto;
import com.sunhy.service.IDIshService;
import com.sunhy.service.IDishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    //新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("新增菜品数据"+dishDto);
        //操作两张表
        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功^_^");
    }
}
