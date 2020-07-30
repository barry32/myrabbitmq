package com.learn.myrabbitmq.basic.publishconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @Description
 * @Author Barry
 * @create: 2020-06-30 16:58
 **/
public class PublishConfirm {
    private static String QUEUE_NAME = "PUBLISH_CONFIRM";
    private static Connection createConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory.newConnection();
    }
    public static void publishConfirmListener() throws IOException, TimeoutException {
        Connection connection = PublishConfirm.createConnection();
        Channel channel = connection.createChannel();
        channel.confirmSelect();
        ConcurrentSkipListMap<Long,String> concurrentNavigableMap = new ConcurrentSkipListMap<>();
        ConfirmCallback confirmCallback = (sequenceNumber, multiple)->{
            if (multiple) {
                ConcurrentNavigableMap headMap = concurrentNavigableMap.headMap(sequenceNumber, true);
                headMap.clear();
            } else {
                concurrentNavigableMap.remove(sequenceNumber);
            }
        };
        channel.addConfirmListener(confirmCallback,(sequenceNumber,multiple)->{
            String message = concurrentNavigableMap.get(sequenceNumber);
            System.out.println(""+message+"has been blocked");
            confirmCallback.handle(sequenceNumber,multiple);
        });
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        long start = System.nanoTime();
        IntStream.rangeClosed(1,50).mapToObj(String::valueOf).forEach(item->{
            concurrentNavigableMap.put(channel.getNextPublishSeqNo(), item);
            try {
                channel.basicPublish("",QUEUE_NAME,null,item.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.format("Published messages and handled confirms asynchronously in %,d ms%n", Duration.ofNanos(end - start).toMillis());
    }
    public static void main(String[] args) throws IOException, TimeoutException {
        publishConfirmListener();
    }
}
