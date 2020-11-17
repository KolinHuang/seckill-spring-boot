package com.yucaihuang.seckillspringboot.controller;

import com.yucaihuang.seckillspringboot.bo.GoodsBo;
import com.yucaihuang.seckillspringboot.pojo.OrderInfo;
import com.yucaihuang.seckillspringboot.pojo.User;
import com.yucaihuang.seckillspringboot.redis.RedisService;
import com.yucaihuang.seckillspringboot.redis.UserKey;
import com.yucaihuang.seckillspringboot.result.CodeMsg;
import com.yucaihuang.seckillspringboot.result.Result;
import com.yucaihuang.seckillspringboot.service.SeckillGoodsService;
import com.yucaihuang.seckillspringboot.service.SeckillOrderService;
import com.yucaihuang.seckillspringboot.utils.CookieUtils;
import com.yucaihuang.seckillspringboot.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/order")
public class SeckillOrderController {

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillOrderService seckillOrderService;

    @Autowired
    SeckillGoodsService seckillGoodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(@RequestParam("orderId") long orderId, HttpServletRequest request){
        String loginToken = CookieUtils.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);
        if(user == null){
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }
        //这里有个BUG，用了订单ID去查询秒杀订单的ID，只有当二者相等时，才能查出结果
        OrderInfo order = seckillOrderService.getOrderInfoById(orderId);
        if(order == null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        Long goodsId = order.getGoodsId();
        GoodsBo goods = seckillGoodsService.getSeckillGoodsBoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setGoods(goods);
        vo.setOrder(order);
        return Result.success(vo);
    }
}
