package com.tzl.entity;


/**
 * MessageType
 * copyright tzl
 * 2018/6/1 17:01
 * version V1.0
 * email ab5822@qq.com
 * 文件描述(用一句话描述该文件做什么)
 * 消息实例
 */
public class Message {
    private String uniqueId;
    private String uid;
    private String oid;
    private Integer time;
    private String routing;
    private String body;

    public String getUniqueId() {
        return uniqueId;
    }
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getRouting() {
        return routing;
    }

    public void setRouting(String routing) {
        this.routing = routing;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
