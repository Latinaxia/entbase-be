package com.example.entbasebe.Service.impl;

import com.example.entbasebe.Service.IShareFileService;
import com.example.entbasebe.Utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class IShareFileServiceImpl implements IShareFileService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result creatShareFile(Integer bucketId, String password, String filePath) {
        //为要共享的文件生成一个唯一的共享ID，并将该ID，文件路径和密码以hash的形式存入redis，设置有效期12h
        Path path = Paths.get(filePath);
        String uniqueStringId = UUID.nameUUIDFromBytes(path.toString().getBytes()).toString().substring(0, 6);
        stringRedisTemplate.opsForHash().put(uniqueStringId, password,filePath);
        stringRedisTemplate.expire(filePath, 12, TimeUnit.HOURS);
        //返回该共享ID，供用户访问共享文件
        log.info("生成的共享ID是：{},该共享文件的密码是：{}", uniqueStringId, password);
        return Result.ok(uniqueStringId);
    }

    @Override
    public Result getShareFile(String shareId, String pwd) {
        //根据共享ID从redis中获取文件路径和密码，并返回文件路径和密码
        String filePath = (String) stringRedisTemplate.opsForHash().get(shareId, pwd);
        log.info("获取的共享文件路径是：{},该共享文件的密码是：{}", filePath, pwd);
        if (filePath == null) {
            return Result.fail("密码错误");
        }
        File file = new File(filePath);
        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.ok(fileBytes);
    }


}
