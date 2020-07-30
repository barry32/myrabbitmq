package com.learn.myrabbitmq.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author Barry
 * @create: 2020-07-28 18:11
 **/
@Slf4j
@Component
public class MessageSender {


    public void sendMessage (final RabbitTemplate rabbitTemplate, String exchange, String routingKey, Object message) {
        log.info("begin sending message {}",message);
        rabbitTemplate.convertAndSend(exchange,routingKey,message);
        log.info("success!");
    }

}
