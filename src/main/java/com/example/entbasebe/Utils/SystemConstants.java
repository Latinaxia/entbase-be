package com.example.entbasebe.Utils;

/*
为常用数据创建常量名
 */
public class SystemConstants {

    /*
    在redis中存储登录验证码code的过期时间
     */
    public static final Long LOGIN_CODE_TTL = 60*10L;


    /*
    jwt令牌signKey,太短的话周周那里会报错，太长的话会导致token过长，影响性能
     */
    public static final String LOGIN_USER_KEY =  "cbc5b166f64b3e50bfeb2f8d833381735c74219de6e91e964aab948f706ca473";

    /*
    存储登录用户信息jwt令牌和过期时间，七天
     */
    public static final Long LOGIN_USER_TTL = 7 * 24 * 60 * 60 * 1000L;

    /*
    默认用户名前缀
     */
    public static final String USER_NICK_NAME_PREFIX = "entbaser_";

    //发送邮箱的邮箱地址，各位请自行修改
    public static final String EMAIL_FROM = "3276327856@qq.com";
    public static final String __RECYCLE_BIN = "__RECYCLE_BIN";

    //存储用户头像的目录
    public static final String __AVATAR = "__AVATAR";
}
