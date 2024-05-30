package com.example.entbasebe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("folder")
@EqualsAndHashCode(callSuper = false)// 重写equals和hashCode方法，保证两个对象相同的属性值也相同
@Accessors(chain = true)// 支持链式调用，setter返回的将为this值而不是void，没有这个习惯可以将chain设为false
public class Folder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "fold_id", type = IdType.AUTO)
    Integer FoldId;

    String FoldName;

    String FoldPath;

    Integer FatherId;//若文件夹有父文件夹，此既是父文件夹的fold_id

    Integer UserId;

    LocalDateTime CreatTime;

    LocalDateTime UpdateTime;
}
