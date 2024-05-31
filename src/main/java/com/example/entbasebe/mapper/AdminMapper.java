package com.example.entbasebe.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Delete("delete from file where user_id = #{userId}")
    void deleteFileByUserId(Integer userId);

    @Delete("delete from folder where user_id = #{userId}")
    void deleteFoldByUserId(Integer userId);

    @Delete("delete from user where user_id = #{userId}")
    void deleteUserById(Integer userId);

    @Select("select fold_path,file_path from file,folder where user_id = #{userId}")
    List<String> selectAllFileAndFolderByUserId(@Param("userId") Integer userId);

}
