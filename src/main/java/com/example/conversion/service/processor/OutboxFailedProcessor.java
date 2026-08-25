package com.example.conversion.service.processor;

import com.example.conversion.kafka.KafkaTopics;
import com.example.conversion.kafka.producer.EventProducer;
import com.example.conversion.model.entity.OutboxTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OutboxFailedProcessor implements OutboxEventProcessor {
    private final KafkaTopics kafkaTopics;
    private final EventProducer eventProducer;

    @Override
    public void execute(OutboxTable outboxTable) {
        eventProducer.sendFileFailedEvent(kafkaTopics.getFileFailed(), outboxTable.getOutboxId(), outboxTable.getPayload());
        log.info("Failed event sent to topic {}", outboxTable.getOutboxId());
    }
}
