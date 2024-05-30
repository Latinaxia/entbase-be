package com.example.entbasebe.Service;

import com.example.entbasebe.DTO.LoginDTO;
import com.example.entbasebe.Utils.Result;

import javax.servlet.http.HttpSession;

public interface IUserService {
    Result login(LoginDTO loginDTO, HttpSession session);
}
