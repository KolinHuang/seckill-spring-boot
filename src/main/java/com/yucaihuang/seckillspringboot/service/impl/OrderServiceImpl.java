package com.yucaihuang.seckillspringboot.service.impl;


import com.yucaihuang.seckillspringboot.dao.OrderInfoMapper;
import com.yucaihuang.seckillspringboot.pojo.OrderInfo;
import com.yucaihuang.seckillspringboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderInfoMapper orderInfoMapper;


    @Override
    public long addOrder(OrderInfo orderInfo) {
        return orderInfoMapper.insert(orderInfo);
    }

    @Override
    public OrderInfo getOrderInfoById(long orderId) {
        return orderInfoMapper.queryOrderInfoById(orderId);
    }
}
