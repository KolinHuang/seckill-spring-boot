package com.yucaihuang.seckillspringboot.vo;

import com.yucaihuang.seckillspringboot.bo.GoodsBo;
import com.yucaihuang.seckillspringboot.pojo.OrderInfo;

public class OrderDetailVo {

    private GoodsBo goods;
    private OrderInfo order;

    public GoodsBo getGoods() {
        return goods;
    }

    public void setGoods(GoodsBo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
