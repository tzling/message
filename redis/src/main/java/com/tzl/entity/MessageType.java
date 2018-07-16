package com.tzl.entity;

/**
 * MessageType
 * copyright tzl
 * 2018/6/1 17:01
 * version V1.0
 * email ab5822@qq.com
 * 文件描述(用一句话描述该文件做什么)
 * 消息类型
 */
public interface MessageType {
    /*
    * 返回消息
    * */
    int Result  = 0;
    int Success = 200;
    int Error   = 404;
    /*
    * 文本消息
    * */
    int Text  = 1;
    /*
    * 图像消息
    * */
    int Image = 2;
    /*
    * 音频消息
    * */
    int Audio = 3;
    /*
    * 登陆消息
    * */
    int Login = 4;
    /*
    * 结束消息
    * */
    int End   = 5;
}
