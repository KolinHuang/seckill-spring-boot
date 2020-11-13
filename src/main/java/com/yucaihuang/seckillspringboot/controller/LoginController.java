package com.yucaihuang.seckillspringboot.controller;

import com.yucaihuang.seckillspringboot.common.Const;
import com.yucaihuang.seckillspringboot.param.LoginParam;
import com.yucaihuang.seckillspringboot.pojo.User;
import com.yucaihuang.seckillspringboot.redis.RedisService;
import com.yucaihuang.seckillspringboot.redis.UserKey;
import com.yucaihuang.seckillspringboot.result.Result;
import com.yucaihuang.seckillspringboot.service.UserService;
import com.yucaihuang.seckillspringboot.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 用户登录控制
 */
@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;


    @PostMapping("/login")
    @ResponseBody
    public Result<User> doLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session, @Valid LoginParam loginParam){
        Result<User> login = userService.login(loginParam);
        if(login.isSuccess()){
            CookieUtils.writeLoginToken(response, session.getId());
            // Redis，将用户名+seesionID作为键，登录的数据作为值放入redis，实现免登录
            redisService.set(UserKey.getByName, session.getId(),login.getData(), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            //免登录的备选，当redis不可用时，用cookie从数据库里读session

            //不好不好，这样实现很不好，我们的目的本来就是把请求拦截在上游，这样搞会让很多请求打到DB的
            //但是还是先放着吧，先把redis当缓存看
            session.setAttribute(session.getId(), loginParam.getMobile());
            //将用户名（电话号码）写入cookie中
            session.setAttribute("userPhone",loginParam.getMobile());
            Cookie cookie = new Cookie("cookie_userphone",loginParam.getMobile());
            cookie.setDomain("localhost");
            cookie.setHttpOnly(true);
            // 设置cookie的持久化时间，30天
            cookie.setMaxAge(30 * 24 * 60 * 60);
            // 设置为当前项目下都携带这个cookie
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return login;
    }
}
