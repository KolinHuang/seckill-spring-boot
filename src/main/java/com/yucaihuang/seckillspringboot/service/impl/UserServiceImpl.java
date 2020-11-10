package com.yucaihuang.seckillspringboot.service.impl;

import com.yucaihuang.seckillspringboot.dao.UserMapper;
import com.yucaihuang.seckillspringboot.param.LoginParam;
import com.yucaihuang.seckillspringboot.pojo.User;
import com.yucaihuang.seckillspringboot.result.CodeMsg;
import com.yucaihuang.seckillspringboot.result.Result;
import com.yucaihuang.seckillspringboot.service.UserService;
import com.yucaihuang.seckillspringboot.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public Result<User> login(LoginParam loginParam) {
        User user = userMapper.checkPhone(loginParam.getMobile());
        //号码未通过
        if (user == null){
            return Result.error(CodeMsg.MOBILE_NOT_EXIST);
        }
        String password = user.getPassword();
        //根据用户的salt生成MD5值，然后跟数据库中的密码数据比对
        String getPassword = MD5Util.formPassToDBPass(loginParam.getPassword(), user.getSalt());
        if(!StringUtils.equals(getPassword,password)){
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
        user.setPassword(StringUtils.EMPTY);
        return Result.success(user);

    }

    @Override
    public User getUserByPhone(String phone) {
        return userMapper.checkPhone(phone);
    }

    @Override
    public int insert(User user) {
        String password = user.getPassword();
        String salt = user.getSalt();
        String pass = MD5Util.inputPassToDbPass(password, salt);
        user.setPassword(pass);
        return userMapper.insert(user);
    }
}
