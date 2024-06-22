package com.example.entbasebe.mapper;
import com.example.entbasebe.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface FileMapper {
    List<File> getFileByPathAndBucketId(@Param(value = "bucketId")Integer bucketId, @Param(value = "path") String path);

    int deleteByPathAndBucketId(@Param(value = "bucketId")Integer bucketId, @Param(value = "path")String path);

    File getOneFileByPathAndBucketId(@Param(value = "path")String sourcePath, @Param(value = "bucketId")Integer bucketId);

    void insert(File file);

    void update(@Param("file") File file, @Param("sourcePath") String sourcePath, @Param("bucketId") Integer bucketId);

    void updateFilePathAndTime(@Param("file") File file);
}
