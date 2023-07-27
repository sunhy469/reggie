package com.sunhy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunhy.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish>    {
}
