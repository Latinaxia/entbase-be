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


//    @Select("select b.bucket_space, b.is_public, f.fold_name, f.fold_path " +
//            "from bucket b join entbase.folder f on b.user_id = f.user_id " +
//            "where b.is_public = '1' or b.user_id = #{userId} and f.is_bucket = 1")
//    List<BucketsDTO> listBuckets(Integer userId);


    @Select("select bucket_id from bucket where user_id = #{userId} or is_public = '1' ")
    List<Integer> listBucketIds(Integer userId);


    @Select("select bucket_id,fold_name,fold_path, bucket_space,is_public " +
            "from folder f join bucket b on f.user_id = b.user_id " +
            "where fold_id = #{bucketId} limit 1")
    BucketsDTO getBucket(Integer bucketId);
}
