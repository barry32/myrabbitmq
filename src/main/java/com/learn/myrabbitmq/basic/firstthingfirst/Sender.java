package com.learn.myrabbitmq.basic.firstthingfirst;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description sender
 * @Author Barry
 * @create: 2020-06-18 11:14
 **/
public class Sender {


    private static final String QUEUE_NAME = "hello";
    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        try (Connection connection = connectionFactory.newConnection();
             Channel channel= connection.createChannel()) {
            channel.basicQos(1);
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            String message = "Hello World!";
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("send successful!");

        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}
