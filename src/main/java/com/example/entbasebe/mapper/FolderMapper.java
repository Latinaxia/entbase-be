package com.example.entbasebe.mapper;
import com.example.entbasebe.entity.Folder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FolderMapper {

    @Insert("insert into entbase.folder (user_id,fold_name,fold_path,is_bucket) values (#{userId}, #{foldName}, #{foldPath}, #{isBucket})")
    void save(Folder folder);

    @Select("select * from entbase.folder where fold_name = #{email}")
    Folder getFoldByName(String email);

    @Update("update entbase.folder set fold_name = #{bucketName} where fold_id = #{bucketId}")
    void updateFolderName(String bucketName, Integer bucketId);

    @Select("select fold_path from entbase.folder where fold_id = #{bucketId}")
    String getFolderPathById(Integer bucketId);

    @Delete("delete from entbase.folder where fold_id = #{bucketId}")
    void deleteFolderById(Integer bucketId);

    @Update("update entbase.folder set fold_path = #{newPath} where fold_id = #{bucketId}")
    void updateFolderPath(String newPath, Integer bucketId);

    List<Folder> getFolderByPath(@Param("path") String path);

    int deleteByPath(@Param("path")String path);

    Integer getIdByPath(@Param("path")String parent);

    void insertOneFolder(Folder folder);

    void updateFolderPathAndTime(@Param("folder")Folder folder);

    void updateFatherId(@Param("fatherId") Integer fatherId, @Param("sourcePath") String sourcePath);

    Folder getOneFolderByPath(@Param("path")String sourcePath);

    @Select("select fold_path from entbase.folder where fold_id = #{bucketId}")
    String getPathByBucketId(Integer bucketId);

    @Update("update entbase.folder set fold_name = #{foldName} where fold_path = #{targetPath}")
    void updateFolderNameByPath(String foldName, String targetPath);
}
