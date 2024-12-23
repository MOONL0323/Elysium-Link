package com.example.demo.content.dao;

import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ContentRedisMapper {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 添加短内容
    public void addShortContent(String key, String content) {
        redisTemplate.opsForValue().set(key, content);
    }

    // 获取短内容
    public String getShortContent(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除短内容
    public void deleteShortContent(String key) {
        redisTemplate.delete(key);
    }

    // 检查短内容是否存在
    public boolean hasShortContent(String key) {
        return redisTemplate.hasKey(key);
    }
}
