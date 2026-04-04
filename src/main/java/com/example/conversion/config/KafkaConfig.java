package com.example.conversion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {
    public DefaultErrorHandler errorHandler() {
        return new DefaultErrorHandler(new FixedBackOff(2000L, 3));
    }
}
