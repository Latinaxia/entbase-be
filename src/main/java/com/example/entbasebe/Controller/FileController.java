package com.example.entbasebe.Controller;
import com.example.entbasebe.Service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class FileController {

    @Resource
    private IFileService fileService;
}
