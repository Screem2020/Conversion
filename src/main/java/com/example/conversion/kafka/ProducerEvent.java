package com.example.conversion.kafka;

import com.example.conversion.dto.FileUpdateEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerEvent {
    private final KafkaTemplate<String, FileUpdateEventDto> kafkaTemplate;

    public void sendFileUpdateEvent(String topic, FileUpdateEventDto fileName) {
        kafkaTemplate.send(topic, fileName.getFileId(), fileName);
    }

}
