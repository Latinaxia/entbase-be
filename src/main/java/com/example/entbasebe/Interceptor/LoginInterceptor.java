package com.example.entbasebe.Interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.entbasebe.DTO.UserHolderDTO;
import com.example.entbasebe.Utils.JwtUtils;
import com.example.entbasebe.Utils.Result;
import com.example.entbasebe.Utils.UserHolder;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Component
@Data
public class LoginInterceptor implements HandlerInterceptor {


    public static String jwtToken;

    public static Result error = Result.fail("NOT_LOGIN");
    public static String notLogin = JSONUtil.toJsonStr(error);
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        //获取请求url
        String url = req.getRequestURL().toString();
        log.info("请求的url: {}",url);

        String method = req.getMethod();
        log.info("请求的方法: {}",method);
        if("OPTIONS".equalsIgnoreCase(method)){
            log.info("OPTIONS请求,放行...");
            return true;
        }

        //2.判断请求url中是否包含login，如果包含，说明是登录操作，放行
        if(url.contains("/login") && url.contains("get") && url.contains("s")){
            log.info("登录/分享文件操作, 放行...");
            return true;
        }

        //3.获取请求头中的令牌,命名为token
        String jwt = req.getHeader("Authorization");

        //4.判断令牌是否存在，如果不存在，返回错误结果（未登录）
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头token为空,返回未登录的信息");
            resp.getWriter().write(notLogin);
            return false;
        }

        //5.解析token，如果解析失败，返回错误结果（未登录）
        UserHolderDTO userHolderDTO;
        try {
            Claims claims = JwtUtils.parseJWT(jwt);
            jwtToken=jwt;
            log.info("令牌{}\n解析成功：{}",jwtToken,claims);
            userHolderDTO = BeanUtil.fillBeanWithMap(claims, new UserHolderDTO(), false);
        } catch (Exception e) {//jwt解析失败
            e.printStackTrace();
            log.info("解析令牌失败, 返回未登录错误信息");
            //手动转换 对象--json --------> 阿里巴巴fastJSON
            resp.getWriter().write(notLogin);
            return false;
        }

        //6.放行。
        log.info("令牌合法, 放行");
        UserHolder.saveUser(userHolderDTO);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(req, response, handler, ex);
    }
}
