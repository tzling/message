package com.tzl.cache;

import java.util.concurrent.TimeUnit;

/**
 * Cache
 * copyright tzl
 * 2018/6/5 17:13
 * version String1.0
 * email ab5822@qq.com
 * 文件描述(用一句话描述该文件做什么)
 * 缓存接口
 */
public interface Cache {
     boolean setCache(String key, String value);
     boolean setCache(String key, String value, long time);
     boolean setCache(String key, String value, long time, TimeUnit seconds);
     String getCache(String key);
     boolean containsKey(String key);
     boolean delete(String key);
}
