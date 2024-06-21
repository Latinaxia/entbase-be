package com.example.entbasebe.DTO;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BucketsDTO {
    Integer BucketSpace;

    String IsPublic;

    @TableId(value = "fold_name")
    String FoldName;

    @TableId(value = "fold_path")
    String FoldPath;

    Integer bucketId;
//    LocalDateTime CreatTime;
//
//    LocalDateTime UpdateTime;
}
