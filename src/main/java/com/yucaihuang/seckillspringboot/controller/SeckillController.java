package com.yucaihuang.seckillspringboot.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.yucaihuang.seckillspringboot.annotation.AccessLimit;
import com.yucaihuang.seckillspringboot.bo.GoodsBo;
import com.yucaihuang.seckillspringboot.common.Const;
import com.yucaihuang.seckillspringboot.mq.MQSender;
import com.yucaihuang.seckillspringboot.mq.SeckillMessage;
import com.yucaihuang.seckillspringboot.pojo.SeckillOrder;
import com.yucaihuang.seckillspringboot.pojo.User;
import com.yucaihuang.seckillspringboot.redis.GoodsKey;
import com.yucaihuang.seckillspringboot.redis.RedisService;
import com.yucaihuang.seckillspringboot.redis.UserKey;
import com.yucaihuang.seckillspringboot.result.CodeMsg;
import com.yucaihuang.seckillspringboot.result.Result;
import com.yucaihuang.seckillspringboot.service.SeckillGoodsService;
import com.yucaihuang.seckillspringboot.service.SeckillOrderService;
import com.yucaihuang.seckillspringboot.utils.CookieUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillGoodsService seckillGoodsService;
    @Autowired
    SeckillOrderService seckillOrderService;

    //创建令牌桶实例
    private RateLimiter rateLimiter = RateLimiter.create(100);

    @Autowired
    MQSender mqSender;

    //将秒杀的商品状态放入Map中
    private ConcurrentHashMap<Long, Boolean> localOverMap = new ConcurrentHashMap<>();


    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsBo> goodsList = seckillGoodsService.getSeckillGoodsList();
        if(goodsList == null){
            return;
        }
        for (GoodsBo goods : goodsList) {
            //缓存预热
            redisService.set(GoodsKey.getSeckillGoodsStock,""+goods.getId(),goods.getStockCount(), Const.RedisCacheExtime.GOODS_LIST);
            //这里有待商榷，少量秒杀商品的情况下，倒是可以用内存来提高效率，但是当商品量非常大时，内存会被占去一大块
            //但是，通常不可能用单体应用来承担所有商品秒杀，应当是已经经过负载均衡后的请求会被放到这里。
            //所以说，当前应用所能查询到的商品是有限的，查询的也应当是多个数据库中的一个。
            //是否使用内存来提高效率，应该视具体情况而定，建议是不要，因为内存很宝贵。
            localOverMap.put(goods.getId(),false);
        }
    }


    @PostMapping("/{path}/seckill")
    @ResponseBody
    public Result<Integer> list(@PathVariable("path") String path, @RequestParam("goodsId") long goodsId, HttpServletRequest request){
        String loginToken = CookieUtils.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);
        if(user == null){
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }
        boolean check = seckillOrderService.checkUrl(user, goodsId, path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        Boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //如果有10个线程同时走到了这里，库存只有9个
        //那么会不会出现并发问题？多个线程会不会同时把9-1呢？不会，decr是原子操作
        Long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        if(stock == null){
            //说明redis发生了故障，
        }
        if(stock < 0){
            //如果有多个线程同时调用这条语句，会不会有线程安全问题？
            //ConcurrentHashMap能保证在put和get时，对于一个槽位的操作是原子的
            //但是在进入这个if块到执行put方法之前，这段时间其他线程很可能get了
            //但是最终还是会走到这里来
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //判断是否之前就已经秒杀到了
        SeckillOrder order = seckillOrderService.getSeckillOrderByUIdAndGId(user.getId(), goodsId);
        if(order != null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //放入队列排队
        SeckillMessage mm = new SeckillMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(mm);
        //在这里显示，您正在排队
        return Result.success(0);


    }

    /**
     * 客户端轮询是否下单成功
     * >0：秒杀成功
     * -1：秒杀失败
     * 0：排队中
     * @param goodsId
     * @param request
     * @return
     */
    @GetMapping("/result")
    @ResponseBody
    public Result<Long> seckillResult( @RequestParam("goodsId") long goodsId, HttpServletRequest request){
        String loginToken = CookieUtils.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);
        if(user == null){
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }

        long result = seckillOrderService.getSeckillResultByUIdAndGId(user.getId(), goodsId);
        return Result.success(result);
    }
    @AccessLimit(seconds = 5,maxCount = 5)
    @GetMapping("/path")
    @ResponseBody
    public Result<String> getSeckillPath(HttpServletRequest request,@RequestParam("goodsId") long goodsId){
        String loginToken = CookieUtils.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);
        if(user == null){
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }
        //如果在1秒内没有获取到令牌，那就抛弃请求
        if(!rateLimiter.tryAcquire(1, TimeUnit.SECONDS)){
            return Result.error(CodeMsg.RATE_LIMIT);
        }
        String url = seckillOrderService.createUrl(user, goodsId);
        return Result.success(url);
    }

}
