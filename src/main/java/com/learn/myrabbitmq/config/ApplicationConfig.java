package com.learn.myrabbitmq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description
 * @Author Barry
 * @create: 2020-07-28 16:50
 **/
@PropertySource("classpath:application.yml")
@Data
@Configuration
public class ApplicationConfig {
    @Value("${barry.exchange}")
    private String exchange;
    @Value("${barry.queue}")
    private String queue;
    @Value("${barry.routing-key}")
    private String routingKey;
}
