package com.tzl.run;

import com.tzl.entity.Config;
import com.tzl.handler.MessageChannelInitializer;
import com.tzl.util.ClassUtils;
import com.tzl.util.ControllerHandler;
import com.tzl.util.ControllerRegistration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.net.ssl.KeyManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.interfaces.RSAKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
public class Connection {
    @Autowired Config config;
    @Autowired ApplicationContext applicationContext;
    private AttributeKey handlerKey = AttributeKey.valueOf("handler");
    private AttributeKey users = AttributeKey.valueOf("userGroups");
    private AttributeKey key = AttributeKey.valueOf("rsaKey");
    protected ConcurrentMap<String,ChannelHandlerContext> userGroups = new ConcurrentHashMap<>();
    protected ConcurrentMap<String,Map<String,RSAKey>> rsaKey = new ConcurrentHashMap<>();
    private ControllerRegistration registration = new ControllerRegistration();
    private ControllerHandler controllerHandler = new ControllerHandler();
    /**
    * 开始程序
    * */
    public void start() throws Exception {
        int maxBuffer = 1 << 20;
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        registration.init();
        controllerHandler.setRequestMapping(registration.getRequestMapping());
        controllerHandler.setApplicationContext(applicationContext);
        controllerHandler.setRsaKey(rsaKey);
        controllerHandler.setUserGroups(userGroups);
        try{
            SslContext sslContext = getSslContext();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MessageChannelInitializer(config.getSplitString(),config.getSeconds(), sslContext, false))
                    .childAttr(handlerKey,controllerHandler)
                    .childAttr(users,userGroups)
                    .childAttr(key,rsaKey)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.SO_SNDBUF, maxBuffer)
                    .childOption(ChannelOption.SO_RCVBUF, maxBuffer)
                    .childOption(ChannelOption.TCP_NODELAY, true);
            ChannelFuture f = bootstrap.bind(config.getPort()).sync();
            System.out.println("connection...");
            f.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
    * 获取ssl密匙
    * */
    private SslContext getSslContext(){
        SslContext sslContext = null;
        if(config.getKeyPath() != null && !config.getKeyPath().equals("")){
            char[] pass = config.getKeyPass().toCharArray();
            try{
                KeyManagerFactory keyManagerFactory;
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(new FileInputStream(config.getKeyPath()), pass);
                keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                keyManagerFactory.init(keyStore,pass);
                sslContext = SslContextBuilder.forServer(keyManagerFactory).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sslContext;
    }

}
