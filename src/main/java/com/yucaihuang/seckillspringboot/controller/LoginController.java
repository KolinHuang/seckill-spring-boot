package com.yucaihuang.seckillspringboot.controller;

import com.yucaihuang.seckillspringboot.param.LoginParam;
import com.yucaihuang.seckillspringboot.pojo.User;
import com.yucaihuang.seckillspringboot.result.Result;
import com.yucaihuang.seckillspringboot.service.UserService;
import com.yucaihuang.seckillspringboot.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping("/login")
    @ResponseBody
    public Result<User> doLogin(HttpServletResponse response, HttpSession session,@Valid LoginParam loginParam){
        Result<User> login = userService.login(loginParam);
        if(login.isSuccess()){
            CookieUtils.writeLoginToken(response, session.getId());
            //TODO Redis
        }
        return login;
    }
}
