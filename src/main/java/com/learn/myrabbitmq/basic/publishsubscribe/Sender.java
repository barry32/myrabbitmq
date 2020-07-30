package com.learn.myrabbitmq.basic.publishsubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @Description
 * @Author Barry
 * @create: 2020-06-22 10:07
 **/
public class Sender {

    private static final String EXCHANGE_NAME = "barry_exchange";
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel();
        ) {
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
            StringBuffer stringBuffer = new StringBuffer("barry");
            IntStream.rangeClosed(1,5).forEach(item->{
                stringBuffer.append(".");
                try {
                    channel.basicPublish(EXCHANGE_NAME,"", MessageProperties.PERSISTENT_TEXT_PLAIN,stringBuffer.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}
