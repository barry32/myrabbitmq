package com.learn.myrabbitmq.basic.workqueues;

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
 * @create: 2020-06-19 10:52
 **/
public class WorkQueueSender {
    private static final String QUEUE_NAME = "work_queue_barry";
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setPassword("guest");
        connectionFactory.setUsername("guest");
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel();
        ) {
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            StringBuffer stringBuffer = new StringBuffer("barry");
            IntStream.rangeClosed(1,5).forEach(item->{
                stringBuffer.append(".");
                try {
                    System.out.println(QUEUE_NAME+" published "+stringBuffer.toString());
                    channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,stringBuffer.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}
