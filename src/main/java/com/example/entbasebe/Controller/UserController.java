package com.example.entbasebe.Controller;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.example.entbasebe.DTO.LoginDTO;
import com.example.entbasebe.Service.IUserService;
import com.example.entbasebe.Utils.Result;
import com.example.entbasebe.entity.ImageCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;


    /**
     * 列出用户的所有存储桶
     * @return
     */
    @PostMapping("/list-buckets")
    public Result listBuckets() {
        return userService.listBuckets();
    }


}
