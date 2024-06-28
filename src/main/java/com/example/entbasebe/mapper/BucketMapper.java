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

    @Select("select bucket_space from bucket where bucket_id = #{bucketId}")
    Float getRemainingSpaceById(Integer bucketId);

    @Update("update bucket set bucket_space = #{difference} where bucket_id = #{bucketId}")
    void updateSpaceByRemainSpaceAndId(@Param("difference") double difference, @Param("bucketId") Integer bucketId);

    @Update("update bucket set bucket_space = bucket_space + #{size} where bucket_id = #{bucketId}")
    void subSpaceByRemainSpaceAndId(@Param("size")float size, @Param("bucketId")Integer bucketId);

    @Insert("insert into bucket (bucket_id,user_id, bucket_space, is_public) values (#{bucketId}, #{userId}, #{bucketSpace}, #{isPublic})")
    void saveBucket(Bucket bucket);

    @Select("select bucket_id from bucket where user_id = #{userId} and is_public = 0")
    Integer getBucketIdByUserId(Integer userId);
}
