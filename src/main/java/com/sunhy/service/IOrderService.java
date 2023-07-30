package com.sunhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunhy.entity.Orders;

public interface IOrderService extends IService<Orders> {

    //用户下单
    void submit(Orders orders);
}
