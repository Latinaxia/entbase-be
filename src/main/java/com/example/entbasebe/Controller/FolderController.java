package com.example.entbasebe.Controller;
import com.example.entbasebe.Service.IFolderService;
import com.example.entbasebe.Utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FolderController {
    @Autowired
    private IFolderService folderService;

}
