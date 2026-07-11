package com.example.conversion.service.processor;

import com.example.conversion.kafka.KafkaTopics;
import com.example.conversion.kafka.producer.EventProducer;
import com.example.conversion.model.entity.OutboxTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OutboxFailedProcessor implements OutboxEventProcessor {
    private final KafkaTopics kafkaTopics;
    private final EventProducer eventProducer;


    @Override
    public void execute(OutboxTable outboxTable) {
        eventProducer.sendFileFailedEvent(kafkaTopics.getFileFailed(), outboxTable.getOutboxId(), outboxTable.getPayload());
    }
}
