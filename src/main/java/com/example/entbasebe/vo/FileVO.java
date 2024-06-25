package com.example.entbasebe.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileVO {
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer userId;
    private String path;
    private String fileName;
    private Boolean isFolder;

}
