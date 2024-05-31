package com.example.entbasebe.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entbasebe.entity.Folder;
import com.example.entbasebe.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FolderMapper {

    @Insert("insert into folder (user_id,fold_name,fold_path,is_bucket) values (#{userId}, #{foldName}, #{foldPath}, #{isBucket})")
    void save(Folder folder);


    @Select("select * from folder where fold_name = #{email}")
    Folder getFoldByName(String email);
}
