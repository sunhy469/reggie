package com.sunhy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunhy.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import javax.management.MXBean;


@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
