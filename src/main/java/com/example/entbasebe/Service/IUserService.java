package com.example.entbasebe.Service;

import com.example.entbasebe.DTO.LoginDTO;
import com.example.entbasebe.Utils.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface IUserService {

    Result login(LoginDTO loginDTO, HttpSession session);

    Result register(LoginDTO loginDTO);

    String saveCodeId(String code);

    Result sendmail(LoginDTO loginDTO);
    
    Result listBuckets();

    Result modifyName(String newName);

    Result modifyPassword(String oldpwd, String newpwd);

    ResponseEntity<byte[]> getAvatar(String userId);

    Result uploadAvatar(MultipartFile icon);

    Result verifyCodeAndResetPassword(String email, String code, String newPassword);

    Result getCodeByEmail(String email);
}
