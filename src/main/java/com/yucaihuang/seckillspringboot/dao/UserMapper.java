package com.yucaihuang.seckillspringboot.dao;

import com.yucaihuang.seckillspringboot.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    public int delete(Long id);

    public int update(User user);

    public int updateSelective(User user);

    public int insert(User user);

    public User queryUserByPhoneAndPassword(@Param("phone") String phone,@Param("password") String password);

    public User checkPhone(@Param("phone") String phone);


}
