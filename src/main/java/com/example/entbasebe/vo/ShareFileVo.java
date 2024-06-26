package com.example.entbasebe.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShareFileVo {
    private Integer userId;

    private String fileName;

    private String userName;

    private LocalDateTime endTime;

    private String filePath;

    private String shareId;

}
