package com.sunhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunhy.dto.SetmealDto;
import com.sunhy.entity.Setmeal;

import java.util.List;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
public interface ISetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
