package com.example.entbasebe.Utils;


import com.example.entbasebe.DTO.UserDTO;
/*
获取当前用户信息的工具类
 */
public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
