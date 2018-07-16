package com.tzl.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzl.entity.Key;
import com.tzl.entity.Message;
import com.tzl.util.RSAUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * MessageType
 * copyright tzl
 * 2018/6/1 17:01
 * version V1.0
 * email ab5822@qq.com
 * 文件描述(用一句话描述该文件做什么)
 * 字符串转实例
 */
public class StringToMessage extends SimpleChannelInboundHandler<String> {
    private final static ObjectMapper mapper  = new ObjectMapper();
    private final static Logger logger = LoggerFactory.getLogger(StringToMessage.class);
    private final static AttributeKey<ConcurrentMap<String,ChannelHandlerContext>> userGroups = AttributeKey.valueOf("userGroups");
    private final static AttributeKey<ConcurrentMap<String,Map<String,RSAKey>>> rsaKey = AttributeKey.valueOf("rsaKey");
    /**
    * 添加连接时返回加密RAS字符
    * 并在将解密密匙保存在map中
    * */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Map<String,RSAKey> clientMap = RSAUtils.instance.getKeys();
        Map<String,RSAKey> serverMap = RSAUtils.instance.getKeys();
        Key key = new Key();
        key.setPublicKey(RSAUtils.instance.getKeyString((PublicKey)serverMap.get("public")));
        key.setPrivateKey(RSAUtils.instance.getKeyString((PrivateKey)clientMap.get("private")));
        serverMap.put("public",clientMap.get("public"));
        ctx.channel().attr(rsaKey).get().put(ctx.channel().remoteAddress().toString(),serverMap);
        ctx.writeAndFlush(mapper.writeValueAsString(key));
    }

    /**
    * 加密字符转实例
    * */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if(msg != null){
            msg = decoder(ctx,msg);
            Message message = mapper.readValue(msg,Message.class);
            if(message != null){
                ctx.fireChannelRead(message);
            }
        }
    }

    /**
     * 关闭异常连接
     * */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String key = ctx.channel().remoteAddress().toString();
        Channel channel = ctx.channel();
        channel.attr(rsaKey).get().remove(key);
        channel.attr(userGroups).get().remove(key);
        channel.close();
        logger.error(cause.getMessage());
    }

    /**
    * 获取存储的密匙
    * */
    private Map<String,RSAKey> getRsaKey(ChannelHandlerContext ctx){
        return ctx.channel().attr(rsaKey).get().get(ctx.channel().remoteAddress().toString());
    }

    /**
    * 解密字符
    * */
    private String decoder(ChannelHandlerContext ctx, String msg) throws Exception {
        Map<String,RSAKey> rsaMap = getRsaKey(ctx);
        RSAPrivateKey pk = (RSAPrivateKey)rsaMap.get("private");
        return RSAUtils.instance.decryptByPrivateKey(msg,pk);
    }
}
