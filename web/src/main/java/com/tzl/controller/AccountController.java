package com.tzl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzl.cache.Cache;
import com.tzl.entity.Account;
import com.tzl.entity.Message;
import com.tzl.routing.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("account")
public class AccountController{

    @Autowired Cache accountCache;
    /**
     * 注册
     * */
    @RequestMapping("registered")
    public Message registered(Message message,String hello) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(message.getBody(), Account.class);
        String ps = accountCache.getCache(account.getAccount());
        if(ps == null){
            boolean bool = accountCache.setCache(account.getAccount(),account.getPassword());
            if(bool){
                message.setBody("success");
            }else{
                message.setBody("account error");
            }
        }else{
            message.setBody("account already exists!");
        }
        message.setTime((int)(System.currentTimeMillis()/1000L));
        return message;
    }

    /**
     * 登陆
     * */
    @RequestMapping("login")
    public Message login(Message message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map loginInfo = mapper.readValue(message.getBody(), HashMap.class);
        String as = accountCache.getCache(loginInfo.get("account").toString());
        if(as != null){
            Account account = mapper.readValue(as,Account.class);
            String p = loginInfo.get("password").toString();
            if(p != null && p.equals(account.getPassword())){
                message.setBody("success");
            }else{
                message.setBody("password error");
            }
        }else{
            message.setBody("account error");
        }
        message.setTime((int)(System.currentTimeMillis()/1000L));
        return message;
    }

    /**
     * 退出
     * */
    @RequestMapping("loginOut")
    public Message loginOut(Message message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(message.getBody(), Account.class);
        if(accountCache.delete(account.getAccount())){
            message.setBody("success");
        }else{
            message.setBody("error");
        }
        return message;
    }

    /**
     * 更新
     * */
    @RequestMapping("update")
    public Message update(Message message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(message.getBody(), Account.class);
        String pass = accountCache.getCache(account.getAccount());
        if(pass != null){
            if(accountCache.setCache(account.getAccount(),account.getPassword())){
                message.setBody("success");
            }else{
                message.setBody("error");
            }
        }else{
            message.setBody("account error");
        }
        return message;
    }
}
