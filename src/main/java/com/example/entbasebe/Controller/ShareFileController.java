package com.example.entbasebe.Controller;


import com.example.entbasebe.Service.IShareFileService;
import com.example.entbasebe.Utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/share")
@RestController
@Slf4j
public class ShareFileController {

    @Resource
    private IShareFileService shareFileService;

    @PostMapping("/create")
    public Result creatShareFile(@RequestParam("bucketId") Integer bucketId,
                                 @RequestParam("password") String password,
                                 @RequestParam("filePath") String filePath) {
        return shareFileService.creatShareFile(bucketId, password,filePath);
    }

    @PostMapping("/get/{shareId}")
    public Result getShareFile(@PathVariable("shareId") String shareId,@RequestParam("password") String pwd) {
        log.info("getShareFile shareId:{},pwd:{}",shareId,pwd);
        return shareFileService.getShareFile(shareId,pwd);
    }
}
