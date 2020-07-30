package com.learn.myrabbitmq.springcloudstream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author Barry
 * @create: 2020-07-30 16:04
 **/
@Slf4j
@Component
@EnableBinding(value = MqProcessor.class)
public class MqInstance {

    private MqProcessor mqProcessor;

    @Autowired
    public MqInstance(MqProcessor mqProcessor) {
        this.mqProcessor = mqProcessor;
    }


    public void sendMessage(Object message) {
        log.info("[spring cloud stream] send message :{}",message);
        mqProcessor.mqSource().send(MessageBuilder.withPayload(message).build());
    }

    @StreamListener(MqProcessor.INPUT_NAME)
    void receiveMessage(Object message){
        log.info("[spring cloud stream] receive message :{}",message);
    }






}
