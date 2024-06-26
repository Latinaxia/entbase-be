package com.example.entbasebe.Controller;
import com.example.entbasebe.DTO.FileMoveDTO;
import com.example.entbasebe.DTO.PathDTO;
import com.example.entbasebe.Service.IFileService;
import com.example.entbasebe.Service.IFolderService;
import com.example.entbasebe.Utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@Slf4j
public class FileController {

    @Resource
    private IFileService fileService;

    @PostMapping("/file/list")
    public Result listFiles(Integer bucketId, String path){

        return fileService.getFiles(bucketId, path);
    }

    @PostMapping("/file/upload")
    public Result uploadFile(String path, Integer bucketId, MultipartFile file){
        return fileService.uploadFile(path, bucketId, file);
    }

    @PostMapping("/file/move")
    public Result moveFile(@RequestBody FileMoveDTO fileMoveDTO){
        return fileService.moveFile(fileMoveDTO);
    }

    @PostMapping("/file/get")
    public ResponseEntity<byte[]> getFile(@RequestBody PathDTO pathDTO){
        return fileService.getFile(pathDTO);
    }

    @PostMapping("/file/delete")
    public Result deleteFile(Integer bucketId, String path){
        return fileService.deleteFile(bucketId, path);
    }

    @PostMapping("/file/create-dir")
    public Result createFolder(Integer bucketId, String path){

        log.info("在桶id为:{}的桶下创建一个路径为：{}的文件夹", bucketId, path);
        return fileService.createFolder(bucketId, path);
    }
}
