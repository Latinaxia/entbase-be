package com.example.entbasebe.Service.impl;

import com.example.entbasebe.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceimpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        System.out.println(message);
        String from = "3276327856@qq.com";
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("验证码");
        message.setText("您的验证码是：" + code);
        mailSender.send(message);
    }
}