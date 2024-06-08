package com.example.entbasebe.Controller;


import com.example.entbasebe.Service.IShareFileService;
import com.example.entbasebe.Utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/share")
@RestController
public class ShareFileController {

    @Resource
    private IShareFileService shareFileService;

    @PostMapping("/create")
    public Result creatShareFile(@RequestParam("bucketId") Integer bucketId,
                                 @RequestParam("password") String password,
                                 @RequestParam("filePath") String filePath) {
        return shareFileService.creatShareFile(bucketId, password,filePath);
    }
}
