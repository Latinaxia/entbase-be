package com.example.entbasebe.Service.impl;

import cn.hutool.core.io.FileUtil;
import com.example.entbasebe.DTO.UserHolderDTO;
import com.example.entbasebe.Service.IFolderService;
import com.example.entbasebe.Utils.Result;
import com.example.entbasebe.Utils.UserHolder;
import com.example.entbasebe.entity.Folder;
import com.example.entbasebe.mapper.FolderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
public class IFolderServiceImpl implements IFolderService {
    @Resource
    private FolderMapper folderMapper;
    @Override
    public Result createFolder(Integer bucketId, String path) {
        path = folderMapper.getPathByBucketId(bucketId) + path;
        UserHolderDTO user = UserHolder.getUser();
        if (user == null){
            return Result.fail("未登录");
        }
        if (bucketId == null){
            return Result.fail("桶ID不能为空！");
        }
        Integer userId = user.getUserId();
        //在本地创建一个文件夹
        if (folderMapper.getOneFolderByPathAndBucketId(path, bucketId) != null){
            return Result.fail("已存在同名文件夹");
        }
        Integer fatherId = folderMapper.getIdByBucketIdAndPath(bucketId, path.substring(0, path.lastIndexOf("/")));
        //添加到数据库中（folder）
        Folder folder = new Folder();
        folder.setCreatTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now())
                .setFatherId(fatherId)
                .setFoldPath(path)
                .setFoldName(FileUtil.getName(path))
                .setIsBucket(0)//0表示不共享
                .setUserId(userId);
        try {
            FileUtil.mkdir(path);
        } catch (Exception e) {
            return Result.fail("创建失败!");
        }
        try {
            folderMapper.insertOneFolder(folder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Result.ok();
    }
}