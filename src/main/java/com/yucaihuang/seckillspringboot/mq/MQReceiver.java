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

    /**
     * 监听队列事件，当有消息时，就处理
     * @param message
     */
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
        //检查是否已经有该订单了，有必要吗？刚刚在放入队列的时候已经检查过一次了
        SeckillOrder order = seckillOrderService.getSeckillOrderByUIdAndGId(user.getId(), goodsId);
        if(order != null){
            return;
        }
        //减库存，下订单，写入秒杀订单
        seckillOrderService.insert(user,goods);
    }
}
