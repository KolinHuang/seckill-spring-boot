package com.yucaihuang.seckillspringboot.mq;

import com.yucaihuang.seckillspringboot.bo.GoodsBo;
import com.yucaihuang.seckillspringboot.pojo.SeckillOrder;
import com.yucaihuang.seckillspringboot.pojo.User;
import com.yucaihuang.seckillspringboot.redis.RedisService;
import com.yucaihuang.seckillspringboot.service.OrderService;
import com.yucaihuang.seckillspringboot.service.SeckillGoodsService;
import com.yucaihuang.seckillspringboot.service.SeckillOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillGoodsService seckillGoodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillOrderService seckillOrderService;

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receive(String message){
        logger.info("receive message:" + message);
        SeckillMessage mm = RedisService.stringToBean(message,SeckillMessage.class);
        User user = mm.getUser();
        long goodsId = mm.getGoodsId();

        GoodsBo goods = seckillGoodsService.getSeckillGoodsBoByGoodsId(goodsId);
        Integer stock = goods.getStockCount();
        if(stock <= 0){
            return;
        }
        SeckillOrder order = seckillOrderService.getSeckillOrderByUIdAndGId(user.getId(), goodsId);
        if(order != null){
            return;
        }
        //减库存，下订单，写入秒杀订单
        seckillOrderService.insert(user,goods);
    }
}
