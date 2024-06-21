package com.example.entbasebe.mapper;

import com.example.entbasebe.entity.Bucket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BucketMapper {

    @Insert("insert into bucket (bucket_id,user_id) values (#{bucketId}, #{userId})")
    void save(Bucket bucket);

    @Select("select fold_path from folder where fold_id = #{bucketId}")
    String getBucketPathById(Integer bucketId);

    @Delete("delete from bucket where bucket_id = #{bucketId}")
    void deleteBucketById(Integer bucketId);

    @Insert("insert into bucket (bucket_id,user_id,bucket_space,is_public) values (#{bucketId}, #{userId}, #{bucketSpace},#{isPublic})")
    void saveBucket(Bucket bucket);
}
