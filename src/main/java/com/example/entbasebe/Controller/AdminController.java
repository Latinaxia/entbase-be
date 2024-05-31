package com.example.entbasebe.Controller;

import com.example.entbasebe.Service.IAdminService;
import com.example.entbasebe.Utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private IAdminService adminService;

    @DeleteMapping("/delete-user")
    public Result deleteUserById(Integer userId){
        log.info("Delete user by id: " + userId);
        return adminService.deleteUserById(userId);
    }
}
