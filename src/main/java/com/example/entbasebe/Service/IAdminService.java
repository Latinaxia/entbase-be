package com.example.entbasebe.Service;

import com.example.entbasebe.Utils.Result;

public interface IAdminService {
    Result deleteUserById(Integer userId);


    Result createPublicBucket(String bucketName);
}
