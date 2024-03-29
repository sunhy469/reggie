package com.sunhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunhy.entity.Category;
import com.sunhy.mapper.CategoryMapper;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
public interface ICategoryService extends IService<Category> {

    public void remove(Long id);
}
