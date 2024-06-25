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
public class PublicController {
    @Resource
    private IUserService userService;

    /**
     * 用户登录
     * @param loginDTO
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        log.info("loginDTO: {}", loginDTO);
        return userService.login(loginDTO, session);
    }

    /**
     * 用户注册
     * @param loginDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody LoginDTO loginDTO) {
        log.info("loginDTO: {}", loginDTO);
        return userService.register(loginDTO);
    }

    /**
     * 给前端返回一个base64格式的图片，前端需要转换为图片验证码
     * @return
     */
    @PostMapping("/get-captcha")
    public Result ImageBase64AndID() {
        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 90, 4, 100);
        // 验证码值
        String code = lineCaptcha.getCode();
        log.info("验证码：{}", code);

        //保存验证码在redis中
        String imageCodeId =  userService.saveCodeId(code);

        //将验证码ID和图片的base64返回到前端
        ImageCode imageCode = new ImageCode(imageCodeId,lineCaptcha.getImageBase64());
        return Result.ok(imageCode);
    }

    /**
     * 发送邮件
     * @param loginDTO
     * @return
     */
    @PostMapping("/get-email-code")
    public Result sendmail(@RequestBody LoginDTO loginDTO) {
        log.info("loginDTO: {}", loginDTO);
        return userService.sendmail(loginDTO);
    }

}
