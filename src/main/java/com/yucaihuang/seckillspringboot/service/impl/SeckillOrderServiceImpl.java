package com.yucaihuang.seckillspringboot.service.impl;

import com.yucaihuang.seckillspringboot.bo.GoodsBo;
import com.yucaihuang.seckillspringboot.common.Const;
import com.yucaihuang.seckillspringboot.dao.SeckillOrderMapper;
import com.yucaihuang.seckillspringboot.pojo.OrderInfo;
import com.yucaihuang.seckillspringboot.pojo.SeckillOrder;
import com.yucaihuang.seckillspringboot.pojo.User;
import com.yucaihuang.seckillspringboot.redis.RedisService;
import com.yucaihuang.seckillspringboot.redis.SeckillKey;
import com.yucaihuang.seckillspringboot.service.OrderService;
import com.yucaihuang.seckillspringboot.service.SeckillGoodsService;
import com.yucaihuang.seckillspringboot.service.SeckillOrderService;
import com.yucaihuang.seckillspringboot.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service("seckillOrderService")
public class SeckillOrderServiceImpl implements SeckillOrderService {

    private final Logger logger =LoggerFactory.getLogger(this.getClass());

    //加入一个混淆字符串（秒杀接口）的salt，为了避免用户猜出我们的md5值，值任意给，越复杂越好
    private final String salt="safjlvllj`asdl.kn";

    @Autowired
    SeckillOrderMapper seckillOrderMapper;

    @Autowired
    SeckillGoodsService seckillGoodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Override
    public SeckillOrder getSeckillOrderByUIdAndGId(long userId, long goodsId) {
        return seckillOrderMapper.querySeckillOrderByUserIdAndGoodsId(userId,goodsId);
    }

    @Transactional
    @Override
    public OrderInfo insert(User user, GoodsBo goodsBo) {
        //减库存
        int success = seckillGoodsService.reduceStock(goodsBo.getId());
        //成功
        if(success == 1){
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setCreateDate(new Date());
            orderInfo.setAddrId(0L);
            orderInfo.setGoodsCount(1);
            orderInfo.setGoodsId(goodsBo.getId());
            orderInfo.setGoodsName(goodsBo.getGoodsName());
            orderInfo.setGoodsPrice(goodsBo.getSeckillPrice());
            orderInfo.setOrderChannel(1);
            orderInfo.setStatus(0);
            orderInfo.setUserId(user.getId());
            //添加信息到订单表，返回订单ID
            long orderId = orderService.addOrder(orderInfo);
            logger.info("orderId -->" +orderId);
            //在秒杀订单表中添加对应条目
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setGoodsId(goodsBo.getId());
            seckillOrder.setOrderId(orderInfo.getId());
            seckillOrder.setUserId(user.getId());
            //插入秒杀表
            seckillOrderMapper.insertSelective(seckillOrder);
            return orderInfo;
        }else {
            setGoodsOver(goodsBo.getId());
            return null;
        }
    }

    /**
     * 从秒杀订单表中查询是否存在此orderID对应的订单
     * 如果没有，返回null
     * 如果有，继续从订单表中查询此订单并返回
     * @param orderId
     * @return
     */
    @Override
    public OrderInfo getOrderInfoById(long orderId) {
        SeckillOrder seckillOrder = seckillOrderMapper.querySeckillOrderById(orderId);
        if(seckillOrder == null){
            return null;
        }

        return orderService.getOrderInfoById(seckillOrder.getOrderId());
    }

    @Override
    public long getSeckillResultByUIdAndGId(Long userId, long goodsId) {
        SeckillOrder seckillOrder = getSeckillOrderByUIdAndGId(userId, goodsId);
        if(seckillOrder != null){
            return seckillOrder.getId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    @Override
    public boolean checkUrl(User user, long goodsId, String path) {
        if(user == null || path == null){
            return false;
        }
        String pathOld = redisService.get(SeckillKey.getSeckillUrl, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(pathOld);
    }

    @Override
    public String createUrl(User user, long goodsId) {
        if(user == null || goodsId <= 0){
            return null;
        }
        //将加盐的秒杀URL放入redis
        String str = MD5Util.md5(UUID.randomUUID() + "123456");
        redisService.set(SeckillKey.getSeckillUrl, ""+user.getId()+"_"+goodsId,str, Const.RedisCacheExtime.GOODS_ID);
        return str;
    }


    /**
     * 设置秒杀商品结束标记
     * @param goodsId
     */
    private void setGoodsOver(Long goodsId){
        redisService.set(SeckillKey.isGoodsOver,""+goodsId,true,Const.RedisCacheExtime.GOODS_ID);
    }

    /**
     * 查看秒杀商品是否已经结束
     * @param goodsId
     * @return
     */
    private boolean getGoodsOver(Long goodsId){
        return redisService.exists(SeckillKey.isGoodsOver,""+goodsId);
    }

}
