package com.tzl.entity;

/**
 * Config
 * copyright tzl
 * 2018/6/5 15:00
 * version V1.0
 * email ab5822@qq.com
 * 文件描述(用一句话描述该文件做什么)
 * 配置文件
 */
public class Config {
    private int port;
    private int seconds;
    private String IP;
    private String nameServerAddr;
    private String keyPath;
    private String keyPass;
    private String splitString;

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getNameServerAddr() {
        return nameServerAddr;
    }

    public void setNameServerAddr(String nameServerAddr) {
        this.nameServerAddr = nameServerAddr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getKeyPass() {
        return keyPass;
    }

    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    public void setSplitString(String splitString) {
        this.splitString = splitString;
    }

    public String getSplitString() {
        return splitString;
    }
}
