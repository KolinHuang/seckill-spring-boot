package com.yucaihuang.seckillspringboot.dao;

import com.yucaihuang.seckillspringboot.pojo.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 秒杀订单
 */
@Mapper
public interface SeckillOrderMapper {

    public int delete(Long id);

    public int insert(SeckillOrder seckillOrder);

    public int update(SeckillOrder seckillOrder);

    public int updateSelective(SeckillOrder seckillOrder);

    public SeckillOrder querySeckillOrderById(Long id);

    /**
     * 根据用户ID和货物ID查询秒杀订单
     * @param userId
     * @param goodsId
     * @return
     */
    public SeckillOrder querySeckillOrderByUserIdAndGoodsId(@Param("userId") Long userId,@Param("goodsId") Long goodsId);
}
