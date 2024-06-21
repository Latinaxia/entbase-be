package com.example.entbasebe.Utils;


import com.example.entbasebe.DTO.UserHolderDTO;

/*
获取当前用户信息的工具类,UserDTO是用户信息的DTO类
 */
public class UserHolder {
    private static final ThreadLocal<UserHolderDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserHolderDTO user){
        tl.set(user);
    }

    public static UserHolderDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
