package com.yucaihuang.seckillspringboot.dao;

import com.yucaihuang.seckillspringboot.redis.GoodsKey;
import com.yucaihuang.seckillspringboot.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author kol Huang
 * @date 2021/4/20
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisService redisService;
    @Test
    public void test(){
//        redisService.set(GoodsKey.getSeckillGoodsStock,"" + 1, 9,300);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + 10);
                if(stock < 0){
                    System.out.println("售空");
                }else{
                    System.out.println(stock);
                }
            }
        };
        for(int i = 0; i < 100; ++i){
            System.out.println("Thread: "+ i+ " start");
            new Thread(runnable).start();

        }
    }
}
