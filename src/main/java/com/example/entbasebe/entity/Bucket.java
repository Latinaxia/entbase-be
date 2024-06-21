package com.example.entbasebe.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


/*
Bucket是一个抽象的概念，Bucket本质上还是一个文件夹
但是由于每个用户的存储空间是有限的，独立的，因此抽象出一个bucket方便管理这个空间
即在用户注册时系统为用户创建一个文件夹，此后用户所有的文件都将存放在此文件夹下，因此Bucket可以理解为用户的文件夹。

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bucket")
public class Bucket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "bucket_id")
    private Integer bucketId;

    @TableId(value = "user_id")
    private Integer userId;

    @TableId(value = "bucket_space")
    private Integer bucketSpace;

    //用于判断这个库是否是公共库，默认为false
    @TableId(value = "is_public")
    private String isPublic;

}
