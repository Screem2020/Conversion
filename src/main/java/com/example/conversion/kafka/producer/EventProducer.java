package com.example.conversion.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaTemplate<UUID, String> kafkaTemplate;

    public void sendFileUpdateEvent(String topic, UUID fileId, String fileName) {
        kafkaTemplate.send(topic, fileId, fileName);
    }

}
