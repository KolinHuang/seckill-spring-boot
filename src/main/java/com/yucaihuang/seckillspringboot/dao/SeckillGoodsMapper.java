package com.yucaihuang.seckillspringboot.dao;

import com.yucaihuang.seckillspringboot.pojo.SeckillGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeckillGoodsMapper {

    public int delete(Long id);

    public int insert(SeckillGoods seckillGoods);

    public int update(SeckillGoods seckillGoods);

    public int updateSelective(SeckillGoods seckillGoods);

    public SeckillGoods querySeckillGoodsById(Long id);

}
