package com.yucaihuang.seckillspringboot.service.impl;

import com.yucaihuang.seckillspringboot.bo.GoodsBo;
import com.yucaihuang.seckillspringboot.dao.GoodsMapper;
import com.yucaihuang.seckillspringboot.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("seckillGoodsService")
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public List<GoodsBo> getSeckillGoodsList() {
        return goodsMapper.queryAllGoodsBo();
    }

    @Override
    public GoodsBo getSeckillGoodsBoByGoodsId(long goodsId) {
        return goodsMapper.queryGoodsBoByGoodsId(goodsId);
    }

    @Override
    public int reduceStock(long goodsId) {
        return goodsMapper.updateStock(goodsId);
    }
}
