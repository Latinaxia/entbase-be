package com.example.entbasebe.Controller;

import com.example.entbasebe.Service.EmailService;
import com.example.entbasebe.Utils.Result;
import com.example.entbasebe.entity.EM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SendMailController {
    @Autowired
    private EmailService emailService;

    @PostMapping ("/send")
    public Result SendMail(@RequestBody EM em){
        log.info("向邮箱{} 发送验证码",em.getTo());
//        String Code= CodeTokenGenerator.generate();
        String Code="123456";
        emailService.sendCode(em.getTo(),Code);
        log.info("验证码{}",Code);
        return Result.ok("验证码已发送！");

    }

}
