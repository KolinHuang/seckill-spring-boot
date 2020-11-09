package com.yucaihuang.seckillspringboot.service;


import com.yucaihuang.seckillspringboot.bo.GoodsBo;
import com.yucaihuang.seckillspringboot.pojo.OrderInfo;
import com.yucaihuang.seckillspringboot.pojo.SeckillOrder;
import com.yucaihuang.seckillspringboot.pojo.User;

/**
 * 秒杀订单业务
 */
public interface SeckillOrderService {

    /**
     * 根据用户ID和商品ID获取秒杀订单信息
     * @param userId
     * @param goodsId
     * @return
     */
    SeckillOrder getSeckillOrderByUIdAndGId(long userId, long goodsId);

    /**
     * 插入一条秒杀订单记录
     * @param user
     * @param goodsBo
     * @return
     */
    OrderInfo insert(User user, GoodsBo goodsBo);

    /**
     * 根据秒杀订单ID获取记录
     * @param orderId
     * @return
     */
    OrderInfo getOrderInfoById(long orderId);

    /**
     * 查询秒杀结果
     * @param userId
     * @param goodsId
     * @return
     */
    long getSeckillResultByUIdAndGId(Long userId, long goodsId);

    /**
     * 检查URL
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    boolean checkUrl(User user, long goodsId, String path);

    /**
     * 创建秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    String createUrl(User user, long goodsId);

}
