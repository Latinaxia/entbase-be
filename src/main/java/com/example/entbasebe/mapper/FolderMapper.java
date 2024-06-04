package com.example.entbasebe.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entbasebe.entity.Folder;
import com.example.entbasebe.entity.User;
import org.apache.ibatis.annotations.*;

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
}
