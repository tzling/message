package com.tzl.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzl.entity.Config;
import com.tzl.routing.RequestMapping;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("message")
public class MessageController {

    @Autowired Config config;

    private DefaultMQProducer producer;
    private final static ObjectMapper mapper = new ObjectMapper();

    private void start() throws MQClientException {
        String group = "MessageProducer";
        producer = new DefaultMQProducer(group);
        producer.setNamesrvAddr(config.getNameServerAddr());
        producer.start();
    }

    @RequestMapping("add")
    public com.tzl.entity.Message add(com.tzl.entity.Message message) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException, JsonProcessingException {
        start();
        Message msg = new Message(config.getIP()+config.getPort(),"",mapper.writeValueAsBytes(message));
        SendResult result = producer.send(msg);
        if(result.getSendStatus() == SendStatus.SEND_OK){
            message.setBody("success");
        }
        shutdown();
        return message;
    }

    private void shutdown(){
        producer.shutdown();
    }

}
