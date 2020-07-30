package com.learn.myrabbitmq.basic.myrpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @Description
 * @Author Barry
 * @create: 2020-06-29 10:34
 **/
public class Server {
    private static final String RPC_QUEUE = "MY_RPC_QUEUE";
    public static int fibonacci(int var) {
        if (0 == var) {
            return 0;
        }
        if (1 == var) {
            return 1;
        }
        return fibonacci(var-1)+fibonacci(var-2);
    }
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(RPC_QUEUE,false,false,false,null);
            channel.basicQos(1);
            Object o = new Object();
            DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
                Integer number = Integer.parseInt(new String(delivery.getBody(), StandardCharsets.UTF_8));
                int result = fibonacci(number);
                System.out.println("fibonacci("+number+")succeed in becoming"+result);
                AMQP.BasicProperties replyProperties = delivery.getProperties();
                channel.basicPublish("",replyProperties.getReplyTo(),replyProperties,String.valueOf(result).getBytes());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                synchronized (o) {
                    o.notify();
                }
            });
            channel.basicConsume(RPC_QUEUE,false,deliverCallback,consumerTag -> {});
            while (true) {
                synchronized (o) {
                    o.wait();
                }
            }
        } catch (TimeoutException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
