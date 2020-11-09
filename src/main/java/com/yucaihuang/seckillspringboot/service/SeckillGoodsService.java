package com.yucaihuang.seckillspringboot.service;


import com.yucaihuang.seckillspringboot.bo.GoodsBo;

import java.util.List;

/**
 * 秒杀商品相关业务
 */
public interface SeckillGoodsService {

    List<GoodsBo> getSeckillGoodsList();

    GoodsBo getSeckillGoodsBoByGoodsId(long goodsId);

    /**
     * 减库存
     * @param goodsId
     * @return
     */
    int reduceStock(long goodsId);
}
