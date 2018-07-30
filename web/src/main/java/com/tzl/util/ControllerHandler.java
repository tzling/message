package com.tzl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzl.entity.Message;
import com.tzl.util.entity.HandlerEntity;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.interfaces.RSAKey;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class ControllerHandler {
    private ConcurrentMap<String,HandlerEntity> requestMapping;
    private ApplicationContext applicationContext;
    private ConcurrentMap<String,ChannelHandlerContext> userGroups;
    private ConcurrentMap<String,Map<String,RSAKey>> rsaKey;

    public void setRequestMapping(ConcurrentMap<String, HandlerEntity> requestMapping) {
        this.requestMapping = requestMapping;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setUserGroups(ConcurrentMap<String, ChannelHandlerContext> userGroups) {
        this.userGroups = userGroups;
    }

    public void setRsaKey(ConcurrentMap<String, Map<String, RSAKey>> rsaKey) {
        this.rsaKey = rsaKey;
    }

    public void handlerMessage(Message message, ChannelHandlerContext context){
        HandlerEntity entity = requestMapping.get(message.getRouting());
        Class clz = applicationContext.getBean(entity.getClassName()).getClass();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map parames = mapper.readValue(message.getBody(),Map.class);
            Method method = clz.getDeclaredMethod(entity.getMethodName());
            Map<String,Type> types = entity.getParameters();
            Object[] objects = new Object[]{};
            for (Map.Entry<String, Type> key : types.entrySet() ) {
                Type type = types.get(key);
            }
            method.invoke(clz,objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
