package com.tzl.cache.Impl;

import com.tzl.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * RedisConnection
 * copyright tzl
 * 2018/6/5 14:17
 * version V1.0
 * email ab5822@qq.com
 * redis操作类
 */
@Component
public class UserCache extends BashCache implements Cache {

    private final static Logger logger = LoggerFactory.getLogger(UserCache.class);
    private final static String USER_KEY_PREFIX = "USER_";

    @Override
    public boolean setCache(String key, String value) {
        return setCache(USER_KEY_PREFIX+key,value,Integer.MAX_VALUE);
    }

    @Override
    public boolean setCache(String key, String value, long time) {
        return setCache(key,value,time,TimeUnit.SECONDS);
    }

    @Override
    public boolean setCache(String key, String value, long time, TimeUnit seconds) {
        try {
            getValueOperations().set(key,value,time,seconds);
            return true;
        }catch (Throwable t){
            logger.error("set cache error,key : "+key);
        }
        return false;
    }

    @Override
    public String getCache(String key) {
        try {
            return getValueOperations().get(key);
        }catch (Throwable t){
            logger.error("get cache error,key : "+key);
        }
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        try {
            return redisTemplate.hasKey(USER_KEY_PREFIX+key);
        }catch (Throwable t){
            logger.error("judgment cache key error,key : "+key);
        }
        return false;
    }

    @Override
    public boolean delete(String key) {
        try {
            redisTemplate.delete(USER_KEY_PREFIX+key);
            return true;
        }catch (Throwable t){
            logger.error("delete cache error,key : "+key);
        }
        return false;
    }
}
