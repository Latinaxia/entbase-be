package com.example.entbasebe.Service;

import com.example.entbasebe.Utils.Result;

public interface IShareFileService {
    Result creatShareFile(Integer bucketId, String password, String filePath);
}
