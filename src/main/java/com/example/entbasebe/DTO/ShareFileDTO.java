package com.example.entbasebe.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShareFileDTO {

    String fileName;

    LocalDateTime startTime;

    LocalDateTime endTime;

    String filePath;

    String shareId;
}
