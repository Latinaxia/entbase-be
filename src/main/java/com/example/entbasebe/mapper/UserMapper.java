package com.example.entbasebe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entbasebe.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where user_email = #{email}")
    User QueryByEmail(String email);

    @Select("select user_id from user where user_email = #{userEmail}")
    Integer getUserIdByEmail(String userEmail);
}
