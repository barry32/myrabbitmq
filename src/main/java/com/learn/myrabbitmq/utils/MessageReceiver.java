package com.learn.myrabbitmq.utils;

import com.learn.myrabbitmq.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author Barry
 * @create: 2020-07-28 18:21
 **/
@Slf4j
@Component
public class MessageReceiver {


    @RabbitListener(queues = "${barry.queue}")
    public void receiveMessage(User user) throws InterruptedException {
        log.info("process the message {}",user);
        Thread.sleep(1000);
        log.info("the work had been done!");
    }
}
