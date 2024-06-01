package com.example.entbasebe.Controller;

import com.example.entbasebe.Service.IAdminService;
import com.example.entbasebe.Utils.Result;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private IAdminService adminService;

    @DeleteMapping("/delete-user")
    public Result deleteUserById(@Param("userId") Integer userId){
        log.info("Delete user by id: " + userId);
        return adminService.deleteUserById(userId);
    }

    @PostMapping("/new-bucket")
    public Result createPublicBucket(@RequestParam("bucketName") String bucketName){
        log.info("Create new Public bucket: " + bucketName);
        return adminService.createPublicBucket(bucketName);
    }


}
