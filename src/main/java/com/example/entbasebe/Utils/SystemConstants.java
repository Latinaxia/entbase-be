package com.example.entbasebe.Utils;

/*
为常用数据创建常量名
 */
public class SystemConstants {

    /*
    在redis中存储登录验证码code和过期时间
     */
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    /*
    在redis中存储登录用户信息token和过期时间
     */
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;

    public static final String USER_NICK_NAME_PREFIX = "entbaser_";

    //发送邮箱的邮箱地址，各位请自行修改
    public static final String EMAIL_FROM = "3276327856@qq.com";
    public static final String __RECYCLE_BIN = "__RECYCLE_BIN";
}
