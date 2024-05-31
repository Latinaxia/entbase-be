package com.example.entbasebe.mapper;

import com.example.entbasebe.entity.Bucket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BucketMapper {

    @Insert("insert into bucket (bucket_id,user_id) values (#{bucketId}, #{userId})")
    void save(Bucket bucket);
}
