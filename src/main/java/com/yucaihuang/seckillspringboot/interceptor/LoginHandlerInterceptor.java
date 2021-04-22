package com.yucaihuang.seckillspringboot.interceptor;

import com.yucaihuang.seckillspringboot.annotation.AccessLimit;
import org.springframework.web.method.HandlerMethod;
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
        }
        return true;
        //接口限流
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
//        if(accessLimit == null) return true;


    }
}
