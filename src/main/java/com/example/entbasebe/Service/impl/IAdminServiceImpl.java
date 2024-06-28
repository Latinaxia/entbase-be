package com.example.entbasebe.Service.impl;

import com.example.entbasebe.DTO.UsersListDTO;
import com.example.entbasebe.Service.IAdminService;
import com.example.entbasebe.Utils.Result;
import com.example.entbasebe.Utils.UserHolder;
import com.example.entbasebe.entity.Bucket;
import com.example.entbasebe.entity.Folder;
import com.example.entbasebe.mapper.AdminMapper;
import com.example.entbasebe.mapper.BucketMapper;
import com.example.entbasebe.mapper.FolderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import static com.example.entbasebe.Utils.SystemConstants.__RECYCLE_BIN;

@Slf4j
@Service
public class IAdminServiceImpl implements IAdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private FolderMapper folderMapper;

    @Resource
    private BucketMapper bucketMapper;

    @Override
    @Transactional
    public Result deleteUserById(Integer userId) {

        //1.在存储层面删除文件和文件夹
        //根据传入的userId，获取该用户bucket的文件夹路径
        String bucketPath = adminMapper.getBucketPathByUserId(userId);
        File bucketFolder = new File(bucketPath);
        log.info("待删除的bucket文件夹路径：{}", bucketPath);

        //递归删除bucket文件夹中的所有文件/文件夹
        try {
            deleteAllFileAndFolder(bucketFolder);
        } catch  (IOException e) {
            throw new RuntimeException("删除bucket文件夹失败！");
        }


        //2.删除数据库中的文件信息，再删文件夹
        adminMapper.deleteFileByUserId(userId);
        adminMapper.deleteFoldByUserId(userId);

        //删除share表中的共享文件
        adminMapper.deleteShareByUserId(userId);

        //3.再删除该用户的bucket
        adminMapper.deleteBucketByUserId(userId);

        //4.最后删除用户
        adminMapper.deleteUserById(userId);

        return Result.ok("已删除该用户的所有信息！");
    }

    @Transactional
    @Override
    public Result createPublicBucket(String bucketName) {
        //1.创建bucket文件夹
        String bucketPath = "."+File.separator+"data" + File.separator + bucketName;
        File bucketFolder = new File(bucketPath);
        if (!bucketFolder.exists()) {
            bucketFolder.mkdirs();
            log.info("创建共享bucket");
        }

        //将该文件夹信息存入数据库，再取出即可获得folderId
        Folder folder = new Folder();
        folder.setFoldName(bucketName);
        folder.setFoldPath(bucketPath);
        folder.setUserId(UserHolder.getUser().getUserId());//管理员user_id,这里因为是管理员创建的，所以直接写死了，管理员是唯一的
        folder.setIsBucket(1);
        folderMapper.save(folder);

        String recyclePath = "."+File.separator+"data" + File.separator + bucketName + File.separator + __RECYCLE_BIN;
        File recycle = new File(recyclePath);
        if (recycle.mkdirs()) {
            log.info(__RECYCLE_BIN + "created successfully");
        }
        //将该文件夹信息存入数据库，再取出即可获得folderId
        Folder recycleFolder = new Folder();
        recycleFolder.setFoldName(__RECYCLE_BIN);
        recycleFolder.setFoldPath(recyclePath);
        recycleFolder.setUserId(UserHolder.getUser().getUserId());
        recycleFolder.setIsBucket(0);
        folderMapper.save(recycleFolder);

        //将共享的bucket构建并存入数据库
        Folder finalFolder = folderMapper.getFoldByName(bucketName);
        Bucket bucket = new Bucket();
        bucket.setUserId(UserHolder.getUser().getUserId());
        bucket.setBucketId(finalFolder.getFoldId());
        bucket.setIsPublic("1");//设置为共享bucket
        bucket.setBucketSpace(2048);
        log.info("创建了一个共享bucket：{}",bucket);

        bucketMapper.saveBucket(bucket);

        return Result.ok("创建共享bucket成功！");
    }

    @Override
    public Result editBucket(String bucketName, Integer bucketId) {
        //将folder表中的名字改掉
        folderMapper.updateFolderName(bucketName, bucketId);

        //将文件夹中folder名字改掉
        //获取原本的bucket路径
        String BucketPath = folderMapper.getFolderPathById(bucketId);
        File folder = new File(BucketPath);
        if (folder.exists()) {
            File newFolder = new File(folder.getParent(), bucketName);
            if (folder.renameTo(newFolder)) {
                System.out.println("文件夹名称修改成功");
            }
        }
        String newBucketPath = folder.getParent() + File.separator + bucketName;
        log.info("新的bucket路径：{}", newBucketPath);

        //更新bucket表中的路径
        folderMapper.updateFolderPath(newBucketPath, bucketId);

        return Result.ok("修改成功！");
    }

    @Override
    public Result deleteBucket(Integer bucketId) {
        //根据bucketId获取bucket路径
        String bucketPath = bucketMapper.getBucketPathById(bucketId);
        log.info("待删除的bucket文件夹路径：{}", bucketPath);
        try {
            //递归删除整个文件夹
            deleteAllFileAndFolder(new File(bucketPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //在数据库中删除bucket信息
        bucketMapper.deleteBucketById(bucketId);
        folderMapper.deleteRecycleBinFolderById(bucketId);
        folderMapper.deleteFolderById(bucketId);

        return Result.ok("删除成功！");
    }

    @Override
    public Result listUsers() {
        List<UsersListDTO> users = adminMapper.listUsers();
        return Result.ok(users);
    }

    public void deleteAllFileAndFolder(File file) throws IOException {
        // 如果是文件夹，则递归删除其内部的所有文件和子文件夹
        if(file.isDirectory()){
            for (File subFile: Objects.requireNonNull(file.listFiles())){
                deleteAllFileAndFolder(subFile);
            }
        }
        // 如果是文件，则直接尝试删除，并检查返回值
        if (!file.delete()) {
            // 使用日志记录器
//            log.error("Failed to delete file: {}", file.getAbsolutePath());
            // 文件删除失败时可以抛出异常或记录错误信息
            throw new IOException("Failed to delete file: " + file.getAbsolutePath());
        }
    }


}
