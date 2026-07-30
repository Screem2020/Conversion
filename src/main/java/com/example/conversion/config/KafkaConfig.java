package com.example.conversion.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> template) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template);
        FixedBackOff backOff = new FixedBackOff(2000L, 3);
        return new DefaultErrorHandler(recoverer, backOff);
    }
//@Bean
//public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> template) {
//
//    DeadLetterPublishingRecoverer recoverer =
//            new DeadLetterPublishingRecoverer(template);
//
//    DefaultErrorHandler handler =
//            new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3));
//
//    handler.setRetryListeners((record, ex, deliveryAttempt) -> {
//        log.error("Kafka processing failed. Attempt {}", deliveryAttempt, ex);
//    });
//
//    return handler;
//}
}
