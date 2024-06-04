package com.example.entbasebe.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();

        //设置redis连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //设置key序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //设置value序列化方式
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));


        //设置序列化器
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
