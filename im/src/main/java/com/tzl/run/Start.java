package com.tzl.run;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Start
 * copyright tzl
 * 2018/6/5 15:22
 * version V1.0
 * email ab5822@qq.com
 * 文件描述(用一句话描述该文件做什么)
 * 测试开始类
 */

public class Start {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        MessageListen messageListen = applicationContext.getBean("messageListen",MessageListen.class);
        messageListen.consumer();
    }
}
