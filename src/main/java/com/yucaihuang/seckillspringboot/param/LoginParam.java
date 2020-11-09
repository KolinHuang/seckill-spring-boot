package com.yucaihuang.seckillspringboot.param;

import com.yucaihuang.seckillspringboot.validator.IsMobile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 登录参数校验
 */
public class LoginParam {

    @NotNull(message =  "手机号码不能为空")
    @IsMobile
    private String mobile;
    @NotNull(message = "密码不能为空")
    @Length(min = 23, message = "密码长度需要在7字符以内")
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
