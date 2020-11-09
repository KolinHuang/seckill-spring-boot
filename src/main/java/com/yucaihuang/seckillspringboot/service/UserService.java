package com.yucaihuang.seckillspringboot.service;


import com.yucaihuang.seckillspringboot.param.LoginParam;
import com.yucaihuang.seckillspringboot.pojo.User;
import com.yucaihuang.seckillspringboot.result.Result;

/**
 * 用户相关业务
 */
public interface UserService {

    Result<User> login(LoginParam loginParam);

    User getUserByPhone(String phone);
}
