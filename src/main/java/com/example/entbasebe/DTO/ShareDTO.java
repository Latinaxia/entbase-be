package com.example.entbasebe.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShareDTO {
    private Integer userId;
    private LocalDateTime endTime;
    private String filePath;
}
