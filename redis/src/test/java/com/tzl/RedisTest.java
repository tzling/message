package com.tzl;

import com.tzl.cache.Cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * RedisTest
 * copyright tzl
 * 2018/6/5 9:36
 * version V1.0
 * email ab5822@qq.com
 * 文件描述(用一句话描述该文件做什么)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-redis.xml")
public class RedisTest
{
    @Autowired
    Cache userCache;

    @Test
    public void init(){
        System.out.println(userCache.setCache("age","29"));
        System.out.println(userCache.getCache("age"));
    }

}