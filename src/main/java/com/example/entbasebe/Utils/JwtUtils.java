package com.example.entbasebe.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String signKey = "zxtnbclass6666";
    private static final Long expire = 43200000L;//有效期12小时

    /**
     * 生成JWT令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @return
     */
    public static String generateJwt(Map<String, Object> claims,Integer userid){
        Map<String, Object> headers = new HashMap<>();
        headers.put("UserId",userid);
        String jwt = Jwts.builder()
                .addClaims(claims)
                .addClaims(headers)
                .signWith(SignatureAlgorithm.HS256, signKey)//signKey太短的话会报错，用长一点的字符串
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public static Claims parseJWT(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();

        return claims;
    }
}
