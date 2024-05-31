package com.example.entbasebe.DTO;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String code;
    private String password;

    private String imageCode;

    //图片验证码的ID而非图片验证码！
    private String imageCodeId;
}
