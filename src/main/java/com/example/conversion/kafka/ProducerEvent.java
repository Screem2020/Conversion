package com.example.conversion.kafka;

import com.example.conversion.dto.FileUpdateEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerEvent {
    private final KafkaTemplate<String, FileUpdateEventDTO> kafkaTemplate;

    public void sendFileUpdateEvent(String topic, FileUpdateEventDTO fileName) {
        kafkaTemplate.send(topic, fileName.getFileId(), fileName);
    }

}
