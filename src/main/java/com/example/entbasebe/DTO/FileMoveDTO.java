package com.example.entbasebe.DTO;

import lombok.Data;

@Data
public class FileMoveDTO {
    private Integer bucketId;
    private String sourcePath;
    private String targetPath;
}
