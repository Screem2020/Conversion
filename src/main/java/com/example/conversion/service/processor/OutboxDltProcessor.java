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
public class OutboxDltProcessor implements OutboxEventProcessor{

    private final EventProducer eventProducer;
    private final KafkaTopics kafkaTopics;

    @Override
    public void execute(OutboxTable outboxTable) {
        eventProducer.sendFileDltEvent(kafkaTopics.getFileDlt(), outboxTable.getOutboxId(), outboxTable.getPayload());
        log.info("Dlt event sent to topic {}", outboxTable.getOutboxId());
    }
}
