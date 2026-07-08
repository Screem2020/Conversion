package com.example.conversion.service;

import com.example.conversion.kafka.producer.EventProducer;
import com.example.conversion.model.entity.OutboxTable;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class SchedulerService {
    @Value("${spring.kafka.topics.file-update}")
    private String fileUpdateTopic;
    private OutboxTableService outboxTableService;
    private EventProducer eventProducer;

    @Scheduled(fixedRate = 5000)
    public void retryTableOutbox() {
        try {
            List<OutboxTable> allEventToList = outboxTableService.findAllEventToList();
            if (allEventToList.isEmpty()) {
                log.info("No events to update");
                return;
            }
            OutboxTable outboxTable = allEventToList.stream()
                    .findFirst()
                    .get();
            log.info("Starting retry table outbox");
            eventProducer.sendFileUpdateEvent(fileUpdateTopic, outboxTable.getId(), outboxTable.getPayload());
        } catch (Exception e) {
            log.info("Retrying table outbox");
        }
    }
}

