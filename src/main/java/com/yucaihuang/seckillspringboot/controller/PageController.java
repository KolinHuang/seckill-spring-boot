package com.yucaihuang.seckillspringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页控制
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @RequestMapping("login")
    public String loginPage(){

        return "login";
    }
}
