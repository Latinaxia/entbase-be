package com.example.entbasebe.Service.impl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entbasebe.DTO.LoginDTO;
import com.example.entbasebe.DTO.UserDTO;
import com.example.entbasebe.Service.IUserService;
import com.example.entbasebe.Utils.JwtUtils;
import com.example.entbasebe.Utils.RegexUtils;
import com.example.entbasebe.Utils.Result;
import com.example.entbasebe.Utils.UserHolder;
import com.example.entbasebe.entity.User;
import com.example.entbasebe.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

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

        //将用户信息存入UserHolder
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        log.info("DTO{}",userDTO);
        UserHolder.saveUser(userDTO);

        //自定义Map,将所有字段的值都转为String,为jwt令牌生成做准备
        Map<String, Object> userMap = BeanUtil.beanToMap(user, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName,fieldValue) -> fieldValue.toString()));

        //使用jwt和user中的所以信息生成token
        String token = JwtUtils.generateJwt(userMap);

        //返回token
        return Result.ok(token);
        // 已弃用
        // 保存用户信息到redis中,并在redis中设置有效期,一个userMap（用户）对应一个token
//        String tokenKey = LOGIN_USER_KEY + token;
//        stringRedisTemplate.opsForHash().putAll(tokenKey,userMap);
//        stringRedisTemplate.expire(tokenKey,LOGIN_USER_TTL, TimeUnit.MINUTES);//有效期


    }
}
