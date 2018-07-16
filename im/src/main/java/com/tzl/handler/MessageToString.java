package com.tzl.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzl.util.RSAUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class MessageToString extends SimpleChannelInboundHandler<Object> {
    private final static ObjectMapper mapper  = new ObjectMapper();
    private final static AttributeKey<ConcurrentMap<String,Map<String,RSAKey>>> key = AttributeKey.valueOf("rsaKey");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        if(obj != null){
            String msg = encoder(ctx,mapper.writeValueAsString(obj));
            ctx.writeAndFlush(msg);
        }
    }

    private ConcurrentMap<String,Map<String,RSAKey>> getRsaKey(ChannelHandlerContext ctx){
        return ctx.channel().attr(key).get();
    }


    private String encoder(ChannelHandlerContext ctx,String encoder) throws Exception {
        ConcurrentMap<String,Map<String,RSAKey>> rasMap = getRsaKey(ctx);
        String address = ctx.channel().remoteAddress().toString();
        Map<String,RSAKey> keyMap = rasMap.get(address);
        RSAPublicKey pk = (RSAPublicKey)keyMap.get("public");
        return RSAUtils.instance.encryptByPublicKey(encoder,pk);
    }
}
