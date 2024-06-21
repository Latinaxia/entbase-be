package com.example.entbasebe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entbasebe.DTO.BucketsDTO;
import com.example.entbasebe.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where user_email = #{email}")
    User QueryByEmail(String email);

    @Select("select user_id from user where user_email = #{userEmail}")
    Integer getUserIdByEmail(String userEmail);


    @Select("select distinct b.bucket_id,b.bucket_space, b.is_public, f.fold_name, f.fold_path " +
            "from bucket b join entbase.folder f on b.bucket_id = f.fold_id " +
            "where (b.is_public = '1') or (b.user_id = #{userId} and f.is_bucket = 1)")
    List<BucketsDTO> listBuckets(Integer userId);

    @Select("select * from user where user_id=#{userId}")
    User getUserById(Integer userId);

}
