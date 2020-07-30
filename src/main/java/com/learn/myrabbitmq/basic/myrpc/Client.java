package com.learn.myrabbitmq.basic.myrpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @Description
 * @Author Barry
 * @create: 2020-06-29 09:34
 **/
public class Client implements AutoCloseable{
    private static final String RPC_QUEUE = "MY_RPC_QUEUE";
    private Connection connection;
    private Channel channel;
    public Client() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();
    }
    @Override
    public void close() throws Exception {
        connection.close();
    }
    public void call(String message) throws IOException, InterruptedException {
        channel.queueDeclare(RPC_QUEUE,false,false,false,null);
        String replyQueueName = channel.queueDeclare().getQueue();
        String corrId = UUID.randomUUID().toString();
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();
        channel.basicPublish("",RPC_QUEUE,properties,message.getBytes());
        BlockingQueue<String>  dataQueue = new ArrayBlockingQueue<>(1);
        String consumeTag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            String correlationId = delivery.getProperties().getCorrelationId();
            if (corrId.equals(correlationId)) {
                String result = new String(delivery.getBody(), StandardCharsets.UTF_8);
                dataQueue.offer(result);
            }
        }, consumerTag -> {
        });
        // block main thread until the result was computed. [*****]
        String result = dataQueue.take();
        // release the consumer when the message was used up.
        channel.basicCancel(consumeTag);
        System.out.println("fibonacci("+message+"), got "+result);
    }
    public static void main(String[] args) {
        try (Client client = new Client()) {
            IntStream.rangeClosed(1,30).forEach(item->{
                try {
                    client.call(String.valueOf(item));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
