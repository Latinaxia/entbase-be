package com.example.entbasebe.Service;

import com.example.entbasebe.Utils.Result;
import org.springframework.http.ResponseEntity;

public interface IShareFileService {
    Result creatShareFile( String password, String filePath);

    ResponseEntity<byte[]> getShareFile(String shareId, String pwd);

    Result listShareFile();

    /**
     * 删除共享
     * @param shareId
     * @return
     */
    Result deleteById(String shareId);

    /**
     * 获取共享信息
     * @param shareId
     * @return
     */
    Result getSharePathById(String shareId);
}
