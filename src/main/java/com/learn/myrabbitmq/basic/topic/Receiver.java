package com.learn.myrabbitmq.basic.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @Description
 * @Author Barry
 * @create: 2020-06-24 16:12
 **/
public class Receiver {
    private static final String EXCHANGE_NAME = "exchange_topic";
    private static final String QUEUE_NAME1 = "queue_topic1";
    private static final String QUEUE_NAME2 = "queue_topic2";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        List<String> routingList = new ArrayList<>();
        routingList.add("china.#");
        routingList.add("american.california.*");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        channel.queueDeclare(QUEUE_NAME1,true,false,false,null);
        channel.queueDeclare(QUEUE_NAME2,true,false,false,null);
        channel.queueBind(QUEUE_NAME1,EXCHANGE_NAME,routingList.get(0));
        channel.queueBind(QUEUE_NAME2,EXCHANGE_NAME,routingList.get(1));
        DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("start to process"+message);
        });
        channel.basicConsume(QUEUE_NAME1,true,deliverCallback,consumerTag -> {});
        channel.basicConsume(QUEUE_NAME2,true,deliverCallback,consumerTag -> {});
    }
}
