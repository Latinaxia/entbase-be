package com.example.entbasebe.Config;

import com.example.entbasebe.Interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录拦截器，拦截需要登录才能查看到的内容
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login").order(0)
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/get-captcha")
                .excludePathPatterns("/user/get-email-code")
                .excludePathPatterns("/share/get/{shareId}");
    }
}
