package com.learn.myrabbitmq.springcloudstream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Description
 * @Author Barry
 * @create: 2020-07-30 16:01
 **/
public interface MqProcessor {
    String INPUT_NAME = "mqSink";
    String OUT_NAME = "mqSource";

    @Input(value = INPUT_NAME)
    SubscribableChannel mqSink();

    @Output(value = OUT_NAME)
    MessageChannel mqSource();
}
