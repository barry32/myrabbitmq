package com.learn.myrabbitmq.controller;

import com.learn.myrabbitmq.config.ApplicationConfig;
import com.learn.myrabbitmq.entity.User;
import com.learn.myrabbitmq.springcloudstream.MqInstance;
import com.learn.myrabbitmq.utils.MessageSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author Barry
 * @create: 2020-07-28 18:26
 **/
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private RabbitTemplate rabbitTemplate;
    private MessageSender messageSender;
    private ApplicationConfig applicationConfig;
    private MqInstance mqInstance;

    public MqInstance getMqInstance() {
        return mqInstance;
    }
    @Autowired
    public void setMqInstance(MqInstance mqInstance) {
        this.mqInstance = mqInstance;
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    @Autowired
    public UserController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @PostMapping
    public Object createUser (@RequestBody User user) {

        mqInstance.sendMessage(user);
        /*messageSender.sendMessage(rabbitTemplate,applicationConfig.getExchange(),applicationConfig.getRoutingKey(),user);*/
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
