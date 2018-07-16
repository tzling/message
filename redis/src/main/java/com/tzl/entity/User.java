package com.tzl.entity;

/**
 * User
 * copyright tzl
 * 2018/6/5 15:33
 * version V1.0
 * email ab5822@qq.com
 * 文件描述(用一句话描述该文件做什么)
 * 用户类
 */
public class User {
    private String remoteAddress;
    private String serverAddress;
    private Integer time;

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
