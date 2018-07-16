package com.tzl.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;

import javax.net.ssl.SSLEngine;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * MessageType
 * copyright tzl
 * 2018/6/1 17:01
 * version V1.0
 * email ab5822@qq.com
 * 文件描述(用一句话描述该文件做什么)
 * 通道初始化
 */
public class MessageChannelInitializer extends ChannelInitializer<SocketChannel> {
    private String end;
    private int seconds;
    private final SslContext context;
    private final boolean startTls;

    public MessageChannelInitializer(String stringSplit, int timeout, SslContext context, boolean startTls) {
        this.end = stringSplit;
        this.seconds = timeout;
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        if(context != null){
            SSLEngine engine = context.newEngine(socketChannel.alloc());
            engine.setUseClientMode(false);
            pipeline.addFirst("ssl",new SslHandler(engine,startTls));
        }
        ByteBuf delimiter = Unpooled.copiedBuffer(end.getBytes());
        pipeline.addLast("framer" ,new DelimiterBasedFrameDecoder(2048,delimiter));
        pipeline.addLast("timeout",new IdleStateHandler(0,0,seconds, TimeUnit.SECONDS));
        pipeline.addLast("decoder",new StringDecoder(Charset.forName("UTF-8")));
        pipeline.addLast("encoder",new StringEncoder(Charset.forName("UTF-8")));
        pipeline.addLast(new StringToMessage(),new ServerHandler(),new MessageToString());
    }
}
