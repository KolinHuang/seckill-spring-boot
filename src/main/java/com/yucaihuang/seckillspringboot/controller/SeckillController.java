package com.yucaihuang.seckillspringboot.controller;

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

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillGoodsService seckillGoodsService;
    @Autowired
    SeckillOrderService seckillOrderService;


    @Autowired
    MQSender mqSender;

    //将秒杀的商品状态放入Map中
    private HashMap<Long, Boolean> localOverMap = new HashMap<>();


    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsBo> goodsList = seckillGoodsService.getSeckillGoodsList();
        if(goodsList == null){
            return;
        }
        for (GoodsBo goods : goodsList) {
            redisService.set(GoodsKey.getSeckillGoodsStock,""+goods.getId(),goods.getStockCount(), Const.RedisCacheExtime.GOODS_LIST);
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
        Long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        if(stock < 0){
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

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @GetMapping("/path")
    @ResponseBody
    public Result<String> getSeckillPath(HttpServletRequest request,@RequestParam("goodsId") long goodsId){
        String loginToken = CookieUtils.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);
        if(user == null){
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }
        String url = seckillOrderService.createUrl(user, goodsId);
        return Result.success(url);
    }

}
