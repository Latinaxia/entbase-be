package com.example.entbasebe.Controller;
import com.example.entbasebe.DTO.LoginDTO;
import com.example.entbasebe.Service.IUserService;
import com.example.entbasebe.Utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
public class UserController {
    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO, HttpSession session){
        log.info("loginDTO: {}", loginDTO);
        return userService.login(loginDTO, session);
    }

}
