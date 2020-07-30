package com.learn.myrabbitmq.basic.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description
 * @Author Barry
 * @create: 2020-06-19 11:22
 **/
public class WorkQueueReceiver {
    private static final String QUEUE_NAME = "work_queue_barry";
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setPassword("guest");
        connectionFactory.setUsername("guest");
        try{
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            channel.basicQos(1);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                try {
                    doWork(message);
                } finally {
                    System.out.println(message +" is Done");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                }
            };
            channel.basicConsume(QUEUE_NAME,false,deliverCallback,consumerTag ->{});
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
    private static void doWork(String string) {
        try {
            for (char c: string.toCharArray()) {
                if ('.'== c) {
                    Thread.sleep(1000);
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
