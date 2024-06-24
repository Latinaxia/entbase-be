package com.example.entbasebe.mapper;
import com.example.entbasebe.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper {
    List<File> getFileByPath(@Param("path") String path);

    int deleteByPathAndBucketId(@Param("bucketId")Integer bucketId, @Param("path")String path);

    File getOneFileByPathAndBucketId(@Param("path")String sourcePath, @Param("bucketId")Integer bucketId);

    void insert(File file);

    void update(@Param("file") File file, @Param("sourcePath") String sourcePath, @Param("bucketId") Integer bucketId);

    void updateFilePathAndTime(@Param("file") File file);
}
