package com.tzl.cache.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;


/**
 * BashCache
 * copyright tzl
 * 2018/6/5 15:50
 * version V1.0
 * email ab5822@qq.com
 * 文件描述(用一句话描述该文件做什么)
 * 缓存类
 */
public class BashCache{

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    ValueOperations<String,String> getValueOperations(){
        return redisTemplate.opsForValue();
    }
}
