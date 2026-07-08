package com.example.conversion.service;

import com.example.conversion.kafka.producer.EventProducer;
import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxStatus;
import com.example.conversion.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerService {
    @Value("${spring.kafka.topics.file-update}")
    private String fileUpdateTopic;
    private final OutboxTableService outboxTableService;
    private final EventProducer eventProducer;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void publishOutboxEvents() {
        try {
            List<OutboxTable> nextEvent = outboxTableService.eventOutboxToList();
            if (nextEvent.isEmpty()) {
                log.info("No events to update");
                return;
            }
            OutboxTable outboxTable = nextEvent.getFirst();
            log.info("Starting retry table outbox");
            eventProducer.sendFileUpdateEvent(fileUpdateTopic, outboxTable.getId(), outboxTable.getPayload());
            outboxTable.setStatus(OutboxStatus.SUCCESS); // стоит вынести отдельно

        } catch (Exception e) {
            log.error("Retrying table outbox",e);
        }
    }
}

