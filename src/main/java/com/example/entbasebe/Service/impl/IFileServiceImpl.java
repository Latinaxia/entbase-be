package com.example.entbasebe.Service.impl;

import cn.hutool.core.io.FileUtil;
import com.example.entbasebe.DTO.FileMoveDTO;
import com.example.entbasebe.DTO.PathDTO;
import com.example.entbasebe.DTO.UserHolderDTO;
import com.example.entbasebe.Service.IFileService;
import com.example.entbasebe.Utils.Result;
import com.example.entbasebe.Utils.UserHolder;
import com.example.entbasebe.entity.User;
import com.example.entbasebe.mapper.UserMapper;
import com.example.entbasebe.vo.FileVO;
import com.example.entbasebe.entity.File;
import com.example.entbasebe.entity.Folder;
import com.example.entbasebe.mapper.BucketMapper;
import com.example.entbasebe.mapper.FileMapper;
import com.example.entbasebe.mapper.FolderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class IFileServiceImpl implements IFileService {
    @Resource
    private FileMapper fileMapper;
    @Resource
    private FolderMapper folderMapper;
    @Resource
    private BucketMapper bucketMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public Result getFiles(Integer bucketId,String path) {

        //如果没有传入bucketId，就根据用户id查bucketId
        if(bucketId == null) {
            //获取当前用户的id
            Integer userId = UserHolder.getUser().getUserId();
            bucketId = bucketMapper.getBucketIdByUserId(userId);
        }

        Result legal = isLegal(UserHolder.getUser(), bucketId, path);
        if (legal != null){
            return legal;
        }

        path = folderMapper.getPathByBucketId(bucketId) + (path.equals("/") ? "" : path);
        //获取所有以path开头的文件和文件夹
        List<com.example.entbasebe.entity.File> fileList = fileMapper.getFileByPath(path);
        List<Folder> folderList = folderMapper.getFolderByPath(path);
        ArrayList<FileVO> fileVOList = new ArrayList<>();
        //对于文件夹，筛选其中父路径完全等于path的文件夹
        for (Folder folder : folderList) {
            String foldPath = folder.getFoldPath();
            int lastIndexOf = foldPath.lastIndexOf("/");
            String fatherPath = foldPath.substring(0, lastIndexOf);
            if (Objects.equals(fatherPath, path)){
                Integer userId = folder.getUserId();
                User user = userMapper.getUserById(userId);
                FileVO fileVO = FileVO.builder()
                        .path(folder.getFoldPath())
                        .userId(userId)
                        .fileName(folder.getFoldName())
                        .createTime(folder.getCreatTime())
                        .updateTime(folder.getUpdateTime())
                        .isFolder(true)
                        .userName(user.getUserName())
                        .userEmail(user.getUserEmail())
                        .build();
                fileVOList.add(fileVO);
            }
        }
        //对于文件，筛选其中父路径完全等于path的文件
        for (File file : fileList) {
            String filePath = file.getFilePath();
            int lastIndexOf = filePath.lastIndexOf("/");
            String fatherPath = filePath.substring(0, lastIndexOf);
            if (Objects.equals(fatherPath, path)){
                Integer userId = file.getUserId();
                User user = userMapper.getUserById(userId);
                FileVO fileVO = FileVO.builder()
                        .userId(userId)
                        .fileName(file.getFileName())
                        .createTime(file.getCreatTime())
                        .updateTime(file.getUpdateTime())
                        .path(file.getFilePath())
                        .isFolder(false)
                        .userName(user.getUserName())
                        .userEmail(user.getUserEmail())
                        .build();
                fileVOList.add(fileVO);
            }
        }
        //返回结果及其大长度
        return Result.ok(fileVOList, (long) fileVOList.size());
    }

    @Override
    @Transactional
    public Result deleteFile(Integer bucketId, String path) {
        //如果没有传入bucketId，就根据用户id查bucketId
        if(bucketId == null) {
            //获取当前用户的id
            Integer userId = UserHolder.getUser().getUserId();
            bucketId = bucketMapper.getBucketIdByUserId(userId);
        }
        path = folderMapper.getPathByBucketId(bucketId) + path;
        Result legal = isLegal(UserHolder.getUser(), bucketId, path);
        if (legal != null){
            return legal;
        }
        java.io.File file = new java.io.File(path);
        //计算文件大小（MB）
        float size = (float) file.length() / 1024 / 1024;
        //在本地删除
        if (!FileUtil.del(file)){
            return Result.fail("删除失败！可能原因是没有这样的路径。");
        }
        //在数据库删除(file & folder)
        try {
            fileMapper.deleteByPath(path);
            folderMapper.deleteByPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //释放空间
        bucketMapper.subSpaceByRemainSpaceAndId(size, bucketId);
        return Result.ok();
    }

    @Override
    public ResponseEntity<byte[]> getFile(PathDTO pathDTO) {
        Integer bucketId = pathDTO.getBucketId();
        //如果没有传入bucketId，就根据用户id查bucketId
        if(bucketId == null) {
            //获取当前用户的id
            Integer userId = UserHolder.getUser().getUserId();
            bucketId = bucketMapper.getBucketIdByUserId(userId);
        }
        String path = pathDTO.getPath();
        path = folderMapper.getPathByBucketId(bucketId) + path;
        Result legal = isLegal(UserHolder.getUser(), bucketId, path);
        if (legal != null){
            return Result.fail("404","服务器错误！");
        }

        //返回文件的二进制
        java.io.File file = new java.io.File(path);

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getName());

        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);

    }

    @Override
    @Transactional
    public Result moveFile(FileMoveDTO fileMoveDTO) {
        Integer bucketId = fileMoveDTO.getBucketId();
        //如果没有传入bucketId，就根据用户id查bucketId
        if(bucketId == null) {
            //获取当前用户的id
            Integer userId = UserHolder.getUser().getUserId();
            bucketId = bucketMapper.getBucketIdByUserId(userId);
        }
        String sourcePath = fileMoveDTO.getSourcePath();
        String targetPath = fileMoveDTO.getTargetPath();
        sourcePath = folderMapper.getPathByBucketId(bucketId) + sourcePath;
        targetPath = folderMapper.getPathByBucketId(bucketId) + targetPath;
        String finalPath;
        Result legal = isLegal(UserHolder.getUser(), bucketId, sourcePath, targetPath);
        if (legal != null){
            return legal;
        }
        //如果targetPath已经存在，则拒绝移动
        if (fileMapper.getOneFileByPath(targetPath) != null ||
                folderMapper.getOneFolderByPath(targetPath) != null){
            return Result.fail("目标路径已存在！");
        }
        try {
            //处理数据库
            //获取targetPath的parent部分
            String tParent = targetPath.substring(0, targetPath.lastIndexOf("/"));
            //根据parent获取foldId
            Integer foldId = folderMapper.getIdByPath(tParent);
            //10. 修改fold_path = sourcePath的文件夹的fatherId = foldId
            folderMapper.updateFatherId(foldId, sourcePath);
            if (folderMapper.getOneFolderByPath(sourcePath) != null) {
                //文件夹
                //1. 找出fold_path = sourcePath的folder
                //2. 获取该folder的parent
                String sParent = sourcePath.substring(0, sourcePath.lastIndexOf("/"));
                //3. 找出所有以sourcePath开头的文件夹
                List<Folder> folderList = folderMapper.getFolderByPath(sourcePath);
                //4. 采用循环，将这些文件夹的fold_path减去开头的parent得到新的path
                for (Folder fd : folderList) {
                    String foldPath = fd.getFoldPath();
                    int index = foldPath.indexOf(sParent);
                    String newPath = foldPath.substring(index + sParent.length());
                    //6. 将第四步和第五步的结果进行合并得到最终path
                    finalPath = tParent + newPath;
                    fd.setFoldPath(finalPath).setUpdateTime(LocalDateTime.now());
                    //7. 将path、time等修改存入数据库
                    folderMapper.updateFolderPathAndTime(fd);
                }
                //8. 找出所有以sourcePath开头的文件
                List<File> fileList = fileMapper.getFileByPath(sourcePath);
                //9. 执行第4、5、6、7步
                for (File fi : fileList) {
                    String filePath = fi.getFilePath();
                    int index = filePath.indexOf(sParent);
                    String newPath = filePath.substring(index + sParent.length());
                    finalPath = tParent + newPath;
                    fi.setFilePath(finalPath).setUpdateTime(LocalDateTime.now());
                    fileMapper.updateFilePathAndTime(fi);
                }
            } else {
                //文件
                File file = new File();
                file.setFilePath(targetPath)
                        .setFoldId(foldId)
                        .setUpdateTime(LocalDateTime.now())
                        .setFileName(targetPath.substring(targetPath.lastIndexOf("/") + 1));
                fileMapper.update(file, sourcePath);
            }
            //本地移动文件（夹）
            Files.move(Path.of(sourcePath), Path.of(targetPath));
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return Result.ok();
    }

    @Override
    @Transactional
    public Result uploadFile(String path, Integer bucketId, MultipartFile multipartFile) {

        //如果没有传入bucketId，就根据用户id查bucketId
        if(bucketId == null) {
            //获取当前用户的id
            Integer userId = UserHolder.getUser().getUserId();
            bucketId = bucketMapper.getBucketIdByUserId(userId);
        }

        path = folderMapper.getPathByBucketId(bucketId) + path;
        UserHolderDTO user = UserHolder.getUser();
        Result legal = isLegal(user, bucketId, path);
        if (legal != null){
            return legal;
        }
        if (multipartFile.isEmpty()) {
            return Result.fail("上传的文件不能为空！");
        }
        if(fileMapper.getOneFileByPath(path) != null){
            return Result.fail("已存在同名文件");
        }
        Integer userId = user.getUserId();
        //1. 获取剩余桶空间（MB）
        Float remainSpace = bucketMapper.getRemainingSpaceById(bucketId);
        //2. 如果剩余桶空间为零或者剩余桶空间减去上传文件大小的差值小于零，则上传失败
        float difference = (float) (remainSpace - (multipartFile.getSize() / 1024.0 / 1024.0));
        if (remainSpace == 0 || difference < 0){
            return Result.fail("存储空间不足！");
        }
        //5. 否则，差值为新的剩余桶空间并修改bucket表中的对应记录
        bucketMapper.updateSpaceByRemainSpaceAndId(difference, bucketId);
        try {
            //6. file表插入一条记录
            File file = new File();
            String foldPath = path.substring(0, path.lastIndexOf("/"));
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            Integer foldId = folderMapper.getIdByPath(foldPath);
            file.setUpdateTime(LocalDateTime.now())
                    .setCreatTime(LocalDateTime.now())
                    .setFilePath(path)
                    .setFileName(fileName)
                    .setUserId(userId)
                    .setFoldId(foldId);
            fileMapper.insert(file);
            //7. 在本地实现文件上传
            multipartFile.transferTo(Path.of(path));
        } catch (Exception e) {
            throw new RuntimeException("未知原因导致文件上传失败！");
//            return Result.fail("未知原因导致文件上传失败！");
        }

        return Result.ok();
    }

    @Override
    public Result createFolder(Integer bucketId, String path) {
        //如果没有传入bucketId，就根据用户id查bucketId
        if(bucketId == null) {
            //获取当前用户的id
            Integer userId = UserHolder.getUser().getUserId();
            bucketId = bucketMapper.getBucketIdByUserId(userId);
        }
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
        if (folderMapper.getOneFolderByPath(path) != null){
            return Result.fail("已存在同名文件夹");
        }
        Integer fatherId = folderMapper.getIdByPath(path.substring(0, path.lastIndexOf("/")));
        //添加到数据库中（folder）
        Folder folder = new Folder();
        folder.setCreatTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now())
                .setFatherId(fatherId)
                .setFoldPath(path)
                .setFoldName(FileUtil.getName(path))
                .setIsBucket(0)
                .setUserId(userId);
        try {
            if (!new java.io.File(path).mkdir()){
                throw new RuntimeException();
            }
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

    @Override
    public Result getSpace(Integer bucketId){
        try {
            log.info("开始获取空间大小");
            //如果没有传入bucketId，就根据用户id查bucketId
            if(bucketId == null) {
                log.info("bucketId为空，根据用户id查找bucketId");
                //获取当前用户的id
                Integer userId = UserHolder.getUser().getUserId();
                bucketId = bucketMapper.getBucketIdByUserId(userId);
                if(bucketId == null) {
                    log.error("无法找到用户的bucketId");
                    return Result.fail("无法找到用户的bucketId");
                }
            }
            Integer space = fileMapper.getSpaceByBucketId(bucketId);
            if(space == null) {
                log.error("无法获取bucket的空间大小");
                return Result.fail("无法获取bucket的空间大小");
            }
            log.info("成功获取空间大小");
            return Result.ok(space);
        } catch (Exception e) {
            log.error("获取空间大小时出现异常", e);
            return Result.fail("获取空间大小时出现异常");
        }
    }


    //判断用户参数是否合法
    private Result isLegal(UserHolderDTO user, Integer bucketId, String... path) {
        if (user == null){
            return Result.fail("未登录");
        }
        if (bucketId == null){
            return Result.fail("桶ID不能为空！");
        }
        for (String p : path) {
            if (p == null){
                return Result.fail("路径不能为空!");
            }
        }
        return null;
    }
}