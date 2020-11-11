package com.yucaihuang.seckillspringboot.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userPhone = request.getSession().getAttribute("userPhone");
        if(userPhone == null){
            request.getRequestDispatcher("/page/login").forward(request,response);
            return false;
        }else {
            return true;
        }
    }
}
