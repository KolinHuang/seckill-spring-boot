package com.yucaihuang.seckillspringboot.controller;

import com.yucaihuang.seckillspringboot.bo.GoodsBo;
import com.yucaihuang.seckillspringboot.pojo.User;
import com.yucaihuang.seckillspringboot.result.CodeMsg;
import com.yucaihuang.seckillspringboot.result.Result;
import com.yucaihuang.seckillspringboot.service.SeckillGoodsService;
import com.yucaihuang.seckillspringboot.service.UserService;
import com.yucaihuang.seckillspringboot.utils.CookieUtils;
import com.yucaihuang.seckillspringboot.vo.GoodsDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SeckillGoodsService seckillGoodsService;

    @Autowired
    UserService userService;

    @RequestMapping("/list")
//    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model){
        List<GoodsBo> seckillGoodsList = seckillGoodsService.getSeckillGoodsList();
        model.addAttribute("goodsList",seckillGoodsList);
        return "goods_list";
        //TODO 提高加载速度，先从从redis中获取缓存的页面，如果没有，手动渲染页面再返回
        //https://www.cnblogs.com/xiufengchen/p/11653726.html
    }

    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(Model model, @PathVariable("goodsId") long goodsId, HttpServletRequest request){
        //TODO redis读取用户，并返回给前端
        String loginToken = CookieUtils.readLoginToken(request);

        //查找用户的初步实现，通过Cookie保存用户名实现，在redis不可用的情况下使用cookie读取
        Cookie[] cookies = request.getCookies();
        String cookie_userphone = null;
        for (Cookie cookie : cookies) {
            if("cookie_userphone".equals(cookie.getName())){
                cookie_userphone = cookie.getValue();
            }
        }
        User user = userService.getUserByPhone(cookie_userphone);



        GoodsBo goods = seckillGoodsService.getSeckillGoodsBoByGoodsId(goodsId);
        if(goods == null){
            //没有查询到货物信息
            return Result.error(CodeMsg.NO_GOODS);
        }else {
            model.addAttribute("goods",goods);
            long startTime = goods.getStartDate().getTime();
            long endTime = goods.getEndDate().getTime();
            long now = System.currentTimeMillis();

            int miaoshaStatus = 0;
            int remainSeconds = 0;
            if(now < startTime){
                miaoshaStatus = 0;
                remainSeconds = (int) ((startTime - now) / 1000);
            }else if( now > endTime){
                miaoshaStatus = 2;
                remainSeconds = -1;
            }else {
                miaoshaStatus = 1;
                remainSeconds = 0;
            }

            GoodsDetailVo vo = new GoodsDetailVo();
            vo.setGoods(goods);
            vo.setUser(user);
            vo.setMiaoshaStatus(miaoshaStatus);
            vo.setRemainSeconds(remainSeconds);
            return Result.success(vo);
        }


    }
}
