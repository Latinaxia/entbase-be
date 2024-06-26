package com.example.entbasebe.mapper;

import com.example.entbasebe.DTO.ShareDTO;
import com.example.entbasebe.DTO.ShareFileDTO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Mapper
public interface ShareFileMapper {

    //由于MyBatis的版本问题，在某些版本的MyBatis中，如果方法参数是基本类型或者包装类型，要用@Param注解，SQL才能引用参数。
    @Insert("insert into share (user_id, share_id, password, file_path, end_time) values (#{userId}, #{uniqueStringId}, #{password}, #{filePath}, #{endTime})")
    void saveShareFile(@Param("userId") Integer userId, @Param("uniqueStringId") String uniqueStringId, @Param("password") String password, @Param("filePath") String filePath, @Param("endTime") LocalDateTime endTime);

    @Select("select end_time from share where share_id = #{shareId}")
    LocalDateTime getEndTime(String shareId);

    @Select("select file_path from share where share_id = #{shareId} and password = #{pwd}")
    String getFilePath(String shareId, String pwd);


    @Select("select share_id, file_path, end_time from share where user_id = #{userId}")
    ArrayList<ShareFileDTO> listShareFile(Integer userId);

    @Select("select file_name from file where file_path = #{filePath}")
    String getFileName(String filePath);

    /**
     * 删除共享
     * @param shareId
     * @param userId
     */
    @Delete("delete from share where share_id=#{shareId} and user_id=#{userId}")
    void deleteById(String shareId, Integer userId);

    /**
     * 获取共享目录/路径
     * @param shareId
     * @return
     */
    @Select("select user_id,end_time,file_path from share where share_id=#{shareId}")
    ShareDTO getShareFileById(String shareId);

    @Select("select share_id, file_path, end_time from share")
    ArrayList<ShareFileDTO> listAllShareFile();
}
