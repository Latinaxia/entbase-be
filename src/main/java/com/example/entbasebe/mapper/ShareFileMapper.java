package com.example.entbasebe.mapper;

import com.example.entbasebe.DTO.ShareDTO;
import com.example.entbasebe.DTO.ShareFileDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Mapper
public interface ShareFileMapper {

    @Insert("insert into share (user_id, share_id, password, file_path, end_time) values (#{userId}, #{uniqueStringId}, #{password}, #{filePath}, #{endTime})")
    void saveShareFile(Integer userId, String uniqueStringId, String password, String filePath, LocalDateTime endTime);

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
}
