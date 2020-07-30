package com.learn.myrabbitmq.basic.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @Description
 * @Author Barry
 * @create: 2020-06-22 15:44
 **/
public class Receiver {
    private static final String EXCHANGE_NAME = "routing_barry";
    private static final String ROUTING_NAME = "debug";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue,EXCHANGE_NAME,ROUTING_NAME);
        DeliverCallback deliverCallback = (consumeTag, delivery)-> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("receive "+message);
        };
        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
    }
}
