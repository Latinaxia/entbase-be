package com.example.entbasebe.Service.impl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entbasebe.DTO.BucketsDTO;
import com.example.entbasebe.DTO.LoginDTO;
import com.example.entbasebe.DTO.UserDTO;
import com.example.entbasebe.Service.IUserService;
import com.example.entbasebe.Utils.JwtUtils;
import com.example.entbasebe.Utils.RegexUtils;
import com.example.entbasebe.Utils.Result;
import com.example.entbasebe.Utils.UserHolder;
import com.example.entbasebe.entity.Bucket;
import com.example.entbasebe.entity.Folder;
import com.example.entbasebe.entity.User;
import com.example.entbasebe.mapper.BucketMapper;
import com.example.entbasebe.mapper.FolderMapper;
import com.example.entbasebe.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.entbasebe.Utils.SystemConstants.*;

@Slf4j
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    @Resource
    private FolderMapper folderMapper;

    @Resource
    private BucketMapper bucketMapper;

    @Resource
    private JavaMailSender mailSender;


    @Override
    public Result login(LoginDTO loginDTO, HttpSession session) {
        //校验邮箱
        String email = loginDTO.getEmail();
        if(RegexUtils.isEmailInvalid(email)){
            return Result.fail("邮箱格式不正确!");
        }


        //判断用户是否存在
        User user = query().eq("user_email",email).one();
//        User user = userMapper.QueryByEmail(email);
        log.info("{}",user);
        if(user == null){
            return Result.fail("用户不存在!");
        }

        //校验密码
        String password = loginDTO.getPassword();
        if(password == null || password.isEmpty()){
            return Result.fail("密码不能为空!");
        }

        if(!password.equals(user.getUserPassword())){
            return Result.fail("密码错误!");
        }

        String imageCode = loginDTO.getImageCode();
        String imageCodeId = loginDTO.getImageCodeId();
        String redisCode = stringRedisTemplate.opsForValue().get(imageCodeId);
        if(redisCode == null || !redisCode.equals(imageCode)){
            return Result.fail("验证码错误!");
        }

        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        log.info("DTO{}",userDTO);

        //自定义Map,将所有字段的值都转为String,为jwt令牌生成做准备
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName,fieldValue) -> fieldValue.toString()));

        //使用jwt和user中的所以信息生成token
        String token = JwtUtils.generateJwt(userMap,user.getUserId());

        //返回token
        return Result.ok(token);
    }

    @Override
    public Result register(LoginDTO loginDTO) {
        //校验邮箱
        String email = loginDTO.getEmail();
        if(RegexUtils.isEmailInvalid(email)){
            return Result.fail("邮箱格式不正确!");
        }

        //校验密码
        String password = loginDTO.getPassword();
        if(password == null || password.isEmpty()){
            return Result.fail("密码不能为空!");
        }

        //校验验证码
        String code = loginDTO.getCode();
        if(code == null || code.isEmpty()){
            return Result.fail("验证码不能为空!");
        }
        //从redis中获取验证码，与用户输入的验证码进行比对
        String redisCode = stringRedisTemplate.opsForValue().get(email);
        if(redisCode == null || !redisCode.equals(code)){
            return Result.fail("验证码错误!");
        }

        //判断用户是否存在
        User user = query().eq("user_email",email).one();
        if(user != null){
            return Result.fail("用户已存在!");
        }

        //验证码正确，邮箱密码符合规范，则注册用户
        User newUser = new User();
        newUser.setUserEmail(email);
        newUser.setUserPassword(loginDTO.getPassword());
        //用户名随机生成
        newUser.setUserName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(6));
        //注册时默认头像为空
//        String icon = "." + File.separator + "epoch.jpg";
        newUser.setIcon("icon");
        //注册用户不为管理员，isadmin设为0
        newUser.setIsAdmin("0");
        //先存入数据库中，为该用户生自动生成UserId
        save(newUser);


        //将该用户再次取出，为其生成token
        User finalUser = query().eq("user_email",email).one();

        //为该用户创建一个文件夹用于存储该用户的所有文件
        String folderPath = "."+File.separator+ "data" + File.separator + email;
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
            log.info("Folder created successfully.");
        }

        //将该文件夹信息存入数据库，再取出即可获得folderId
        Folder folder = new Folder();
        folder.setFoldName(email);
        folder.setFoldPath(folderPath);
        folder.setUserId(finalUser.getUserId());
        folder.setIsBucket(1);
        folder.setCreatTime(LocalDateTime.now());
        folder.setUpdateTime(LocalDateTime.now());
        folderMapper.save(folder);
        /*可以写成一个函数：initFolder(folderPath, folderPath, 1);*/
        //为该用户创建一个文件夹用作回收站
        String recyclePath = "."+File.separator+"data" + File.separator + email + File.separator + __RECYCLE_BIN;
        File recycle = new File(recyclePath);
        recycle.mkdirs();
        log.info(__RECYCLE_BIN + "created successfully");

        //将该文件夹信息存入数据库，再取出即可获得folderId
        Folder recycleFolder = new Folder();
        recycleFolder.setFoldName(__RECYCLE_BIN);
        recycleFolder.setFoldPath(recyclePath);
        recycleFolder.setUserId(finalUser.getUserId());
        recycleFolder.setIsBucket(0);
        folderMapper.save(recycleFolder);

        //将用户的bucket构建并存入数据库
        Folder finalFolder = folderMapper.getFoldByName(email);
        Bucket bucket = new Bucket();
        bucket.setUserId(finalUser.getUserId());
        bucket.setBucketId(finalFolder.getFoldId());
        bucketMapper.save(bucket);

        //自定义Map,将所有字段的值都转为String,为jwt令牌生成做准备
        Map<String, Object> userMap = BeanUtil.beanToMap(newUser, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName,fieldValue) -> fieldValue.toString()));

        //使用jwt和user中的所以信息生成token
        String token = JwtUtils.generateJwt(userMap,finalUser.getUserId());

        //返回token
        return Result.ok(token);
    }

    @Override
    public String saveCodeId(String code) {
        //存入redis中，用于校验验证码,有效期10分钟
        Random random = new Random();
        String codeId = String.valueOf(1000 + random.nextInt(9000));
        stringRedisTemplate.opsForValue().set(codeId,code,LOGIN_CODE_TTL, TimeUnit.SECONDS);
        return codeId;
    }


    @Override
    public Result listBuckets() {

//        UserHolder.saveUser(new UserHolderDTO(11,"entbaser_g8b0fc","默认头像","3276327856@qq.com","0"));
        //获取当前用户的id
//        Integer userId = UserHolder.getUser().getUserId();

        //判断是否是管理员
        String isAdmin = UserHolder.getUser().getIsAdmin();
        List<BucketsDTO> buckets = new ArrayList<>();
        if(isAdmin.equals("0")){
            buckets = userMapper.listBuckets();
        }
        else{
            buckets = userMapper.listAllBuckets();
        }

        log.info("用户可见的存储桶信息：{}",buckets);

        return Result.ok(buckets);
    }

    @Override
    public Result modifyName(String newName) {
        Integer userId = UserHolder.getUser().getUserId();
        userMapper.modifyName(newName,userId);
        return Result.ok("修改成功!");
    }

    @Override
    public Result modifyPassword(String oldpwd, String newpwd) {
        Integer userId = UserHolder.getUser().getUserId();
        String oldPassword = userMapper.queryPassword(userId);
        if(!oldPassword.equals(oldpwd)){
            return Result.fail("旧密码错误!");
        }
        userMapper.modifyPassword(newpwd,userId);
        return Result.ok("修改成功!");
    }

    @Override
    public ResponseEntity<byte[]> getAvatar(String userId) {
        //根据userId获取头像存储路径
        String icon = userMapper.queryIcon(userId);
        if (icon.equals("icon")){
            return Result.fail("404","头像不存在!");
        }
        //根据路径读取头像文件
        File file = new File(icon);
        log.info("头像路径：{}",icon);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.parse(file.getName()));

        if(file.exists()){
            try {
                byte[] IconBytes = Files.readAllBytes(file.toPath());
                return new ResponseEntity<>(IconBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Result.fail("400","获取头像失败!");
    }

    @Override
    @Transactional
    public Result uploadAvatar(MultipartFile newIcon) {

        if (newIcon.isEmpty()) {
            return Result.fail("上传失败，请选择文件");
        }

        String directoryPath = "." + File.separator + "data" + File.separator + "__AVATAR";
        File directory = new File(directoryPath);

// 检查目录是否存在，如果不存在则创建
        if (!directory.exists()) {
            boolean result = directory.mkdirs();
            if (result) {
                System.out.println("目录已成功创建: " + directoryPath);
            } else {
                System.out.println("无法创建目录: " + directoryPath);
            }
        } else {
            System.out.println("目录已存在: " + directoryPath);
        }


        Integer userId = UserHolder.getUser().getUserId();
        String fileName = UUID.randomUUID().toString().replace("-", "").substring(0, 8)+ "_" + newIcon.getOriginalFilename();
        String icon = "." + File.separator + "data" + File.separator + __AVATAR + File.separator + fileName;
        Path path = Paths.get(icon);
        try {
            Files.copy(newIcon.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            userMapper.saveIcon(userId,icon);
        } catch (IOException e) {
            // 处理 IOException
            e.printStackTrace();
        } catch (SecurityException e) {
            // 处理 SecurityException
            e.printStackTrace();
        }

        return Result.ok("上传头像成功!");
    }

    @Override
    public Result sendmail(LoginDTO loginDTO) {

        //校验邮箱
        String email = loginDTO.getEmail();
        if(RegexUtils.isEmailInvalid(email)){
            return Result.fail("邮箱格式不正确!");
        }

        //根据前端传来的验证码ID在redis中找到验证码
        //将redis中的验证码与前端传来的验证码比对，一致则发送邮箱验证码
        String imageCode = loginDTO.getImageCode();
        String imageCodeId = loginDTO.getImageCodeId();
        String redisCode = stringRedisTemplate.opsForValue().get(imageCodeId);
        if(redisCode == null || !redisCode.equals(imageCode)){
            return Result.fail("验证码错误!");
        }
        //验证码正确，发送邮箱验证码
        //生成邮箱验证码
        String code = RandomUtil.randomNumbers(6);
        //存入redis中，用于校验验证码,有效期10分钟
        stringRedisTemplate.opsForValue().set(email,code,10*60, TimeUnit.SECONDS);
        stringRedisTemplate.expire(email, 10, TimeUnit.MINUTES);
        //发送邮件
        SendMailCode(email,code);
        //返回结果
        return Result.ok("验证码已发送至邮箱！请注意查收");
    }


    private void SendMailCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(EMAIL_FROM);

        message.setTo(email);

        message.setSubject("欢迎使用entbase存储服务!");

        message.setText("您本次请求的邮件验证码为:" + code + ",本验证码 10 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");

        mailSender.send(message);
    }


    @Override
    public Result getCodeByEmail(String email) {
        //验证码正确，发送邮箱验证码
        //生成邮箱验证码
        String code = RandomUtil.randomNumbers(6);
        //存入redis中，用于校验验证码,有效期10分钟
        stringRedisTemplate.opsForValue().set(email,code,10*60, TimeUnit.SECONDS);
        stringRedisTemplate.expire(email, 10, TimeUnit.MINUTES);
        //发送邮件
        SendMailCode(email,code);
        //返回结果
        return Result.ok("验证码已发送至邮箱！请注意查收");
    }

    /**
     * 忘记密码
     * @param email
     * @param code
     * @param newPassword
     * @return
     */
    @Override
    public Result verifyCodeAndResetPassword(String email, String code, String newPassword) {

        //获取用户信息
        Integer userId = userMapper.getUserIdByEmail(email);
        if (userId==null){
            return Result.fail("当前用户不存在");
        }
        //从redis中获取验证码，与用户输入的验证码进行比对
        String redisCode = stringRedisTemplate.opsForValue().get(email);
        if(redisCode == null || !redisCode.equals(code)){
            return Result.fail("验证码错误!");
        }


        //注册
        userMapper.modifyPassword(newPassword,userId);
        return Result.ok("修改成功");
    }


}
