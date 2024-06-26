package com.example.entbasebe.Controller;


import com.example.entbasebe.Service.IShareFileService;
import com.example.entbasebe.Utils.Result;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/share")
@RestController
@Slf4j
public class ShareFileController {

    @Resource
    private IShareFileService shareFileService;

    /**
     * 创建共享文件链接
     * @param bucketId
     * @param password
     * @param filePath
     * @return
     */
    @PostMapping("/create")
    public Result creatShareFile(@RequestParam("bucketId") Integer bucketId,
                                 @RequestParam("password") String password,
                                 @RequestParam("filePath") String filePath) {
        return shareFileService.creatShareFile(bucketId, password,filePath);
    }

    /**
     * 获取共享文件
     *
     * @param shareId
     * @param pwd
     * @return
     */
    @GetMapping("/get/{shareId}/pwd={password}")
    public ResponseEntity<byte[]> getShareFile(@PathVariable("shareId") String shareId, @PathVariable("password") String pwd) {
        log.info("getShareFile shareId:{},pwd:{}",shareId,pwd);
        return shareFileService.getShareFile(shareId,pwd);
    }

    /**
     * 列出用户的所有共享文件
     * @return
     */
    @PostMapping("/list")
    public Result listShareFile(){
        return shareFileService.listShareFile();
    }


    /**
     * 删除共享
     * @param shareId
     * @return
     */
    @PostMapping("/delete/{shareId}")
    public Result deleteById(@PathVariable("shareId") String shareId){
        return shareFileService.deleteById(shareId);
    }


    /**
     * 获取共享信息--返回文件名
     * @param shareId
     * @return
     */
    @PostMapping("/get-info/{shareId}")
    public Result getSharePathById(@PathVariable("shareId") String shareId){
        return shareFileService.getSharePathById(shareId);
    }
}
