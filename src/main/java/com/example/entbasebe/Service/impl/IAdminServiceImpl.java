package com.example.entbasebe.Service.impl;

import com.example.entbasebe.Service.IAdminService;
import com.example.entbasebe.Utils.Result;
import com.example.entbasebe.mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class IAdminServiceImpl implements IAdminService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public Result deleteUserById(Integer userId) {
        //根据传入的userId，删除该用户和该用户的所以文件、文件夹

        //1.在存储层面删除文件和文件夹
        //先获取所以文件和文件夹的路径
        List<String> fileAndFolderPathList = adminMapper.selectAllFileAndFolderByUserId(userId);
        log.info("待删除的文件和文件夹路径列表：{}", fileAndFolderPathList);

        //遍历路径列表，删除文件和文件夹
        for (String fileAndFolderPath : fileAndFolderPathList) {
            File file = new File(fileAndFolderPath);
            if (file.exists()) {
                try {
                    deleteAllFileAndFolder(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        //2.删除数据库中的文件信息，再删文件夹
        adminMapper.deleteFileByUserId(userId);
        adminMapper.deleteFoldByUserId(userId);

        //3.最后删除用户
        adminMapper.deleteUserById(userId);

        return Result.ok("已删除该用户的所以信息！");
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
            log.error("Failed to delete file: {}", file.getAbsolutePath());
            // 文件删除失败时可以抛出异常或记录错误信息
            throw new IOException("Failed to delete file: " + file.getAbsolutePath());
        }
    }

}
