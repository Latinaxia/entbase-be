package com.example.entbasebe.mapper;
import com.example.entbasebe.DTO.UserDTO;
import com.example.entbasebe.DTO.UsersListDTO;
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

    @Select("select fold_path from folder as f join bucket on  bucket.user_id = f.user_id where f.user_id = #{userId}")
    String getBucketPathByUserId(Integer userId);

    @Delete("delete from bucket where user_id = #{userId}")
    void deleteBucketByUserId(Integer userId);

//    @Select("select user_name,icon,user_email,user_email,is_admin from user")
//    List<UserDTO> listUsers();

    @Select("select * from user")
    List<UsersListDTO> listUsers();
}
