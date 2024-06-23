package com.example.entbasebe.mapper;
import com.example.entbasebe.entity.Folder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FolderMapper {

    @Insert("insert into folder (user_id,fold_name,fold_path,is_bucket) values (#{userId}, #{foldName}, #{foldPath}, #{isBucket})")
    void save(Folder folder);


    @Select("select * from folder where fold_name = #{email}")
    Folder getFoldByName(String email);

    @Update("update folder set fold_name = #{bucketName} where fold_id = #{bucketId}")
    void updateFolderName(String bucketName, Integer bucketId);

    @Select("select fold_path from folder where fold_id = #{bucketId}")
    String getFolderPathById(Integer bucketId);

    @Delete("delete from folder where fold_id = #{bucketId}")
    void deleteFolderById(Integer bucketId);

    @Update("update folder set fold_path = #{newPath} where fold_id = #{bucketId}")
    void updateFolderPath(String newPath, Integer bucketId);

    List<Folder> getFolderByPathAndBucketId(@Param("bucketId") Integer bucketId, @Param("path") String path);

    int deleteFolderByIdAndPath(@Param("bucketId")Integer bucketId, @Param("path")String path);

    Integer getIdByBucketIdAndPath(@Param("bucketId")Integer bucketId, @Param("path")String parent);

    void insertOneFolder(Folder folder);

    void updateFolderPathAndTime(@Param("folder")Folder folder);

    void updateFatherId(@Param("fatherId") Integer fatherId, @Param("bucketId") Integer bucketId, @Param("sourcePath") String sourcePath);

    Folder getOneFolderByPathAndBucketId(@Param("path")String sourcePath, @Param("bucketId")Integer bucketId);
}
