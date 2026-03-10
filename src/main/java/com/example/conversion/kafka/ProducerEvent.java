package com.example.conversion.kafka;

import com.example.conversion.dto.FileUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerEvent {
    private final KafkaTemplate<String, FileUpdateEvent> kafkaTemplate;

    public void sendFileUpdateEvent(String topic, FileUpdateEvent fileName) {
        kafkaTemplate.send(topic, fileName);
    }

}
