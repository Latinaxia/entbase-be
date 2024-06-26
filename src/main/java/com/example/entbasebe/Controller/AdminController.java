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
public class AdminController {// www

    @Resource
    private IAdminService adminService;

    /**
     * 通过用户id删除用户信息
     * @param userId
     * @return
     */
    @DeleteMapping("/delete-user")
    public Result deleteUserById(@Param("userId") Integer userId){
        log.info("Delete user by id: " + userId);
        return adminService.deleteUserById(userId);
    }

    /**
     * 创建新的共享桶
     * @param bucketName
     * @return
     */
    @PostMapping("/new-bucket")
    public Result createPublicBucket(@RequestParam("bucketName") String bucketName){
        log.info("Create new Public bucket: " + bucketName);
        return adminService.createPublicBucket(bucketName);
    }

    /**
     * 根据传入的bucketId修改bucketName
     * @param bucketName
     * @param bucketId
     * @return
     */
    @PostMapping("/edit-bucket")
    public Result editBucket(@RequestParam("bucketName") String bucketName, @RequestParam("bucketId") Integer bucketId){
        log.info("Edit bucket: " + bucketId + " to new Name: " + bucketName);
        return adminService.editBucket(bucketName, bucketId);
    }

    /**
     * 删除共享桶
     * @param bucketId
     * @return
     */
    @PostMapping("delete-bucket")
    public Result deleteBucket(@RequestParam("bucketId") Integer bucketId) {
        log.info("Delete bucket: " + bucketId);
        return adminService.deleteBucket(bucketId);
    }

    /**
     * 列出所有用户信息
     * @return
     */
    @PostMapping("/list-users")
    public Result listUsers() {
        return adminService.listUsers();
    }
}
