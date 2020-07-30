package com.learn.myrabbitmq.basic.firstthingfirst;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description
 * @Author Barry
 * @create: 2020-06-18 14:41
 **/
public class Receiver {

    private static final String QUEUE_NAME = "hello";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        Connection connection = connectionFactory.newConnection();
        Channel channel= connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        DeliverCallback deliverCallback = (consumerTag, delivery)-> {

            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("received .... "+message);
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag ->{});
    }
}
