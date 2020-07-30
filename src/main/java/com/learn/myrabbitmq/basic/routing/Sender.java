package com.learn.myrabbitmq.basic.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * @Description
 * @Author Barry
 * @create: 2020-06-22 15:35
 **/
public class Sender {
    private static final String EXCHANGE_NAME = "routing_barry";
    private static final String ROUTING_NAME = "debug";
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME,"direct");
            System.out.println("this is test message");
            channel.basicPublish(EXCHANGE_NAME,ROUTING_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,"this is test message".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
