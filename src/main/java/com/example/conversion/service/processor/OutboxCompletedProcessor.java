package com.example.conversion.service.processor;

import com.example.conversion.kafka.KafkaTopics;
import com.example.conversion.kafka.producer.EventProducer;
import com.example.conversion.model.entity.OutboxTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxCompletedProcessor implements OutboxEventProcessor {
    private final EventProducer eventProducer;
    private final KafkaTopics kafkaTopics;



    @Override
    public void execute(OutboxTable outboxTable) {
        eventProducer.sendFileUpdateEvent(kafkaTopics.getFileUpdate(), outboxTable.getOutboxId(), outboxTable.getPayload());
    }
}
