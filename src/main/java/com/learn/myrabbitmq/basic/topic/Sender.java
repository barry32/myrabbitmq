package com.learn.myrabbitmq.basic.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @Description
 * @Author Barry
 * @create: 2020-06-24 16:12
 **/
public class Sender {
    private static final String EXCHANGE_NAME = "exchange_topic";
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        List<String> routingList = new ArrayList<>();
        routingList.add("china.shanghai.jinan");
        routingList.add("china.shanghai.huangpu");
        routingList.add("china.jiangsu.suzhou");
        routingList.add("american.california.losangeles");
        routingList.add("american.california.sanfrancisco");
        String test = "this is test info";
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME,"topic");
            routingList.forEach(routingKey->{
                try {
                    String  message = test +routingKey;
                    channel.basicPublish(EXCHANGE_NAME,routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}
