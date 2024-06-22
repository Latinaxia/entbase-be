package com.example.entbasebe.Service.impl;

import com.example.entbasebe.DTO.ShareDTO;
import com.example.entbasebe.DTO.ShareFileDTO;
import com.example.entbasebe.DTO.UserHolderDTO;
import com.example.entbasebe.Service.IShareFileService;
import com.example.entbasebe.Utils.Result;
import com.example.entbasebe.Utils.UserHolder;
import com.example.entbasebe.entity.User;
import com.example.entbasebe.mapper.ShareFileMapper;
import com.example.entbasebe.mapper.UserMapper;
import com.example.entbasebe.vo.vo.ShareFileVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;


@Service
@Slf4j
public class IShareFileServiceImpl implements IShareFileService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ShareFileMapper shareFileMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Result creatShareFile(Integer bucketId, String password, String filePath) {
        UserHolder.saveUser(new UserHolderDTO(11,"entbaser_g8b0fc","默认头像","3276327856@qq.com","0"));

        //为要共享的文件生成一个唯一的共享ID，并将该ID，文件路径和密码存入数据库，设置有效期12h
        Path path = Paths.get(filePath);
        String uniqueStringId = UUID.nameUUIDFromBytes(path.toString().getBytes()).toString().substring(0, 6);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endTime = currentTime.plusHours(12);
        //返回该共享ID，供用户访问共享文件
        log.info("生成的共享ID是：{},该共享文件的密码是：{}", uniqueStringId, password);
        //获取用户id，标识这个共享文件属于哪个用户
        Integer userId = UserHolder.getUser().getUserId();

        shareFileMapper.saveShareFile(userId,uniqueStringId, password, filePath,endTime);
        return Result.ok(uniqueStringId);
    }

    @Override
    public Result getShareFile(String shareId, String pwd) {

        //先判断链接有没有过期
        LocalDateTime endTime = shareFileMapper.getEndTime(shareId);
        if(endTime.isBefore(LocalDateTime.now())){
            return Result.fail("分享链接已过期！");
        }

        //再判断密码是否正确
        String filePath = shareFileMapper.getFilePath(shareId,pwd);
        log.info("获取的共享文件路径是：{},该共享文件的密码是：{}", filePath, pwd);
        if (filePath == null) {
            return Result.fail("密码错误");
        }

        //返回文件的二进制
        File file = new File(filePath);
        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.ok(fileBytes);
    }

    @Override
    public Result listShareFile() {
        UserHolder.saveUser(new UserHolderDTO(11,"entbaser_g8b0fc","默认头像","3276327856@qq.com","0"));

        //获取当前用户的id
        Integer userId = UserHolder.getUser().getUserId();
        //根据用户id查询该用户的所有共享文件(文件名暂时空着）
        ArrayList<ShareFileDTO> shareFileDTOS = shareFileMapper.listShareFile(userId);

        //根据文件路径获取文件名,填充
        for(ShareFileDTO x :shareFileDTOS){
            String filePath = x.getFilePath();
            String fileName = shareFileMapper.getFileName(filePath);
            x.setFileName(fileName);

            LocalDateTime endTime = x.getEndTime();
            LocalDateTime startTime = endTime.minusHours(12);
            x.setStartTime(startTime);
        }

        //返回文件名，分享时间，截止日期
        return Result.ok(shareFileDTOS);
    }

    /**
     * 删除共享
     * @param shareId
     * @return
     */
    @Override
    public Result deleteById(String shareId) {
        UserHolder.saveUser(new UserHolderDTO(11,"entbaser_g8b0fc","默认头像","3276327856@qq.com","0"));
        //这里获取用户信息,只能有创建共享者删除

        //获取用户id
        Integer userId = UserHolder.getUser().getUserId();

        //删除共享
        shareFileMapper.deleteById(shareId,userId);

        return Result.ok();
    }

    /**
     * 获取共享信息
     * @param shareId
     * @return
     */
    @Override
    public Result getSharePathById(String shareId) {
        UserHolder.saveUser(new UserHolderDTO(11,"entbaser_g8b0fc","默认头像","3276327856@qq.com","0"));

        // 获取共享文件的路径、创建人ID和结束时间
        ShareDTO shareFile = shareFileMapper.getShareFileById(shareId);
        if (shareFile == null) {
            return Result.fail("共享文件不存在");
        }

        // 获取创建人信息
        User user = userMapper.getUserById(shareFile.getUserId());
        if (user == null) {
            return Result.fail("创建人不存在");
        }

        // 获取文件名
        String fileName = shareFileMapper.getFileName(shareFile.getFilePath());

        ShareFileVo shareFileVo = new ShareFileVo();
        BeanUtils.copyProperties(shareFile,shareFileVo);

        // 设置文件名、创建人和结束时间
        shareFileVo.setFileName(fileName);
        shareFileVo.setUserName(user.getUserName());
        shareFileVo.setEndTime(shareFile.getEndTime());

        return Result.ok(shareFileVo);
    }

}
