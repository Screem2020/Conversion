package com.example.conversion.kafka.producer;

import com.example.conversion.model.dto.FileUpdateEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendFileUpdateEvent(String topic, String fileId, String fileName) {
        kafkaTemplate.send(topic, fileId, fileName);
    }

}
