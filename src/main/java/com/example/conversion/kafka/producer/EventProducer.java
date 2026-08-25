package com.example.conversion.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendFileUpdateEvent(String topic, UUID eventId, String payload) {
        log.info("Sending file update event to topic {} with id {}", topic, eventId);
        kafkaTemplate.send(
                        topic,
                        eventId.toString(),
                        payload);
    }
    public void sendFileFailedEvent(String topic, UUID eventId, String payload) {
        kafkaTemplate.send(
                topic,
                eventId.toString(),
                payload);
    }

    public void sendFileDltEvent(String topic, UUID eventId, String payload) {
        kafkaTemplate.send(
                topic,
                eventId.toString(),
                payload);
    }
}

