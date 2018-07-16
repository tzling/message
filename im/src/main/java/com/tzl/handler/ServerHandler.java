package com.tzl.handler;

import com.tzl.entity.Message;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.BeanFactory;

import java.security.interfaces.RSAKey;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * MessageType
 * copyright tzl
 * 2018/6/1 17:01
 * version V1.0
 * email ab5822@qq.com
 * 文件描述(用一句话描述该文件做什么)
 * 业务类
 */
public class ServerHandler extends SimpleChannelInboundHandler<Message>{
    private final static AttributeKey<BeanFactory> beans = AttributeKey.valueOf("beanFactory");
    private final static AttributeKey<ConcurrentMap<String,ChannelHandlerContext>> userGroups = AttributeKey.valueOf("userGroups");
    private final static AttributeKey<ConcurrentMap<String,Map<String,RSAKey>>> rsaKey = AttributeKey.valueOf("rsaKey");
    /**
     * 心跳检查
     * */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        Channel channel = ctx.channel();
        String key = ctx.channel().remoteAddress().toString();
        System.out.println(key);
        System.out.println(channel.attr(userGroups).get().get(key) == null);
        if(channel.attr(userGroups).get().get(key) == null){
            channel.attr(rsaKey).get().remove(key);
            channel.attr(userGroups).get().remove(key);
            channel.close();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if(msg != null){
            Channel channel = ctx.channel();
            ConcurrentMap<String,ChannelHandlerContext> users = channel.attr(userGroups).get();
            String key = ctx.channel().remoteAddress().toString();
            boolean bool = users.get(key) == null;
            if(msg.getType() == 4 && bool){
                getBeans(ctx,"login");
                System.out.println("login: "+msg.getType());
            }else if(msg.getType() > 0 && msg.getType() < 4 && !bool){
                System.out.println("handler: "+msg.getType());
            }else if(msg.getType() == 5){
                channel.attr(rsaKey).get().remove(key);
                channel.attr(userGroups).get().remove(key);
                ctx.channel().close();
            }
        }
    }

    private Class<?> getBeans(ChannelHandlerContext ctx,String beanName){
        return (Class<?>) ctx.channel().attr(beans).get().getBean(beanName);
    }


}
