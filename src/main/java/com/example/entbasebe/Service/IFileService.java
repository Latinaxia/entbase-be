package com.example.entbasebe.Service;

import com.example.entbasebe.DTO.FileMoveDTO;
import com.example.entbasebe.DTO.PathDTO;
import com.example.entbasebe.Utils.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    Result getFiles(String path);

    Result deleteFile(Integer bucketId, String path);

    ResponseEntity<byte[]> getFile(PathDTO pathDTO);

    Result moveFile(FileMoveDTO fileMoveDTO);

    Result uploadFile(String path, Integer bucketId, MultipartFile file);

    Result createFolder(Integer bucketId, String path);
}
