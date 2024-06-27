package com.example.entbasebe.mapper;
import com.example.entbasebe.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {
    List<File> getFileByPath(@Param("path") String path);

    int deleteByPath(@Param("path")String path);

    File getOneFileByPath(@Param("path")String sourcePath);

    void insert(File file);

    void update(@Param("file") File file, @Param("sourcePath") String sourcePath);

    void updateFilePathAndTime(@Param("file") File file);

    @Select("select bucket_space from bucket where bucket_id = #{bucketId}")
    Integer getSpaceByBucketId(Integer bucketId);
}
