package com.yucaihuang.seckillspringboot.service;

import com.yucaihuang.seckillspringboot.pojo.OrderInfo;

/**
 * 操作订单业务
 */
public interface OrderService {

    long addOrder(OrderInfo orderInfo);

    /**
     * 根据订单ID获取订单信息
     * @param orderId
     * @return
     */
    OrderInfo getOrderInfoById(long orderId);
}
