package com.yucaihuang.seckillspringboot.dao;

import com.yucaihuang.seckillspringboot.bo.GoodsBo;
import com.yucaihuang.seckillspringboot.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {


    /**
     * 返回所有业务对象GoodsBo
     * @return
     */
    public List<GoodsBo> queryAllGoodsBo();

//    public Goods queryById(@Param("goodsId") Long goodsId);

    /**
     * 根据ID查询业务对象GoodsBo
     * @param goodsId
     * @return
     */
    public GoodsBo queryGoodsBoByGoodsId(Long goodsId);

    /**
     * 更新商品
     * @param goods
     * @return
     */
    public int update(Goods goods);

    /**
     * 更新部分商品信息
     * @param goods
     * @return
     */
    public int updateSelective(Goods goods);


    /**
     * 插入商品
     * @param goods
     * @return
     */
    public int insert(Goods goods);

    /**
     * 删除商品
     * @param goodsId
     * @return
     */
    public int deleteById(@Param("goodsId") Long goodsId);

    /**
     * 根据商品ID更新秒杀库存？怎么放在这里写了？
     * @param goodsId
     * @return
     */
    public int updateStock(@Param("goodsId") Long goodsId);

}
