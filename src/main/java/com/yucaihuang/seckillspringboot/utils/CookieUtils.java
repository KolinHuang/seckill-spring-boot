package com.yucaihuang.seckillspringboot.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {


    private static final String COOKIE_DOMAIN = "localhost";
    private static final String COOKIE_NAME = "seckill_login_token";

    private static Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    public static String readLoginToken(HttpServletRequest request){
        //从请求中读取cookies
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            //遍历所有cookie
            for (Cookie cookie : cookies) {
                logger.info("read cookieName:{},cookieValue:{}",cookie.getName(), cookie.getValue());
                //如果cookie名是seckill_login_token，那么就返回此cookie的值
                if(StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    logger.info("return cookieName:{},cookieValue:{}",cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 往response里写token
     * @param response
     * @param token
     */
    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        //把cookie设置在根目录
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        //设置cookie的过期时间，单位是秒
        //如果这个maxage不设置的话，cookie就不会写入硬盘，而是写在内存。只在当前页面有效。
        //如果是-1，表示永久有效
        cookie.setMaxAge(60 * 60 * 24 * 365);
        logger.info("write cookieName:{},cookieValue:{}", cookie.getName(),cookie.getValue());
        response.addCookie(cookie);
    }

    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(StringUtils.equals(cookie.getName(), COOKIE_NAME)){
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);//设置成0，代表删除此cookie
                    logger.info("del cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }
}
