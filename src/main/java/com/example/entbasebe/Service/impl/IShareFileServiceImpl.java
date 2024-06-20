package com.example.entbasebe.Service.impl;

import com.example.entbasebe.DTO.ShareFileDTO;
import com.example.entbasebe.DTO.UserDTO;
import com.example.entbasebe.Service.IShareFileService;
import com.example.entbasebe.Utils.Result;
import com.example.entbasebe.Utils.UserHolder;
import com.example.entbasebe.entity.User;
import com.example.entbasebe.mapper.ShareFileMapper;
import com.example.entbasebe.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class IShareFileServiceImpl implements IShareFileService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ShareFileMapper shareFileMapper;

    @Resource
    private UserMapper userMapperl;

    @Override
    public Result creatShareFile(Integer bucketId, String password, String filePath) {

//        UserHolder.saveUser(new UserDTO("entbaser_g8b0fc","默认头像","3276327856@qq.com","0"));
        //为要共享的文件生成一个唯一的共享ID，并将该ID，文件路径和密码存入数据库，设置有效期12h
        Path path = Paths.get(filePath);
        String uniqueStringId = UUID.nameUUIDFromBytes(path.toString().getBytes()).toString().substring(0, 6);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endTime = currentTime.plusHours(12);
        //返回该共享ID，供用户访问共享文件
        log.info("生成的共享ID是：{},该共享文件的密码是：{}", uniqueStringId, password);
        //获取用户id，标识这个共享文件属于哪个用户
        String userEmail = UserHolder.getUser().getUserEmail();
        Integer userId = userMapperl.getUserIdByEmail(userEmail);

        shareFileMapper.saveShareFile(userId,uniqueStringId, password, filePath,endTime);
        return Result.ok(uniqueStringId);
    }

    @Override
    public Result getShareFile(String shareId, String pwd) {

        //先判断链接有没有过期
        LocalDateTime endTime = shareFileMapper.getEndTime(shareId);
        if(endTime.isBefore(LocalDateTime.now())){
            return Result.fail("分享链接已过期！");
        }

        //再判断密码是否正确
        String filePath = shareFileMapper.getFilePath(shareId,pwd);
        log.info("获取的共享文件路径是：{},该共享文件的密码是：{}", filePath, pwd);
        if (filePath == null) {
            return Result.fail("密码错误");
        }

        //返回文件的二进制
        File file = new File(filePath);
        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.ok(fileBytes);
    }

    @Override
    public Result listShareFile() {
//        UserHolder.saveUser(new UserDTO("entbaser_g8b0fc","默认头像","3276327856@qq.com","0"));
        //获取当前用户的id
        String userEmail = UserHolder.getUser().getUserEmail();
        Integer userId = userMapperl.getUserIdByEmail(userEmail);
        //根据用户id查询该用户的所有共享文件(文件名暂时空着）
        ArrayList<ShareFileDTO> shareFileDTOS = shareFileMapper.listShareFile(userId);

        //根据文件路径获取文件名,填充
        for(ShareFileDTO x :shareFileDTOS){
            String filePath = x.getFilePath();
            String fileName = shareFileMapper.getFileName(filePath);
            x.setFileName(fileName);

            LocalDateTime endTime = x.getEndTime();
            LocalDateTime startTime = endTime.minusHours(12);
            x.setStartTime(startTime);
        }

        //返回文件名，分享时间，截止日期
        return Result.ok(shareFileDTOS);
    }

}
