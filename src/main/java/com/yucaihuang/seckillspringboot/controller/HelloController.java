package com.yucaihuang.seckillspringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author kol Huang
 * @date 2021/3/18
 */
@Controller
public class HelloController {

    @RequestMapping
    public String test(){
        return "hello";
    }
}
