package com.tzl.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzl.cache.Cache;
import com.tzl.entity.Message;
import com.tzl.entity.User;
import io.netty.channel.ChannelHandlerContext;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MessageListen extends Connection {

    @Autowired Cache userCache;

    public void consumer(){
        String group = "MessageConsumer";
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(config.getNameServerAddr());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.BROADCASTING);
        try {
            consumer.subscribe(config.getIP()+config.getPort(), "*");
            final ObjectMapper mapper = new ObjectMapper();
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,ConsumeConcurrentlyContext context) {
                    try {
                        String msg = new String(msgs.get(0).getBody(),"UTF-8");
                        Message message = mapper.readValue(msg,Message.class);
                        if(message != null){
                            User user = mapper.readValue(userCache.getCache(message.getOid()),User.class);
                            if(user != null){
                                ChannelHandlerContext chx = userGroups.get(user.getRemoteAddress());
                                chx.writeAndFlush(message);
                                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            });
            consumer.start();
            System.out.println("start...");
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
