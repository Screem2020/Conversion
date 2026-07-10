package com.example.conversion.service;

import com.example.conversion.kafka.producer.EventProducer;
import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
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
    @Value("${spring.kafka.topics.file-failed}")
    private String fileFailedTopic;
    private final OutboxTableService outboxTableService;
    private final EventProducer eventProducer;

    @Transactional
    @Scheduled(fixedRate = 5000)
    @SchedulerLock(
            name = "publishOutboxEvents",
            lockAtLeastFor ="PT1M",
            lockAtMostFor ="PT10M")
    public void publishOutboxEvents() {
        List<OutboxTable> nextEvent = outboxTableService.eventOutboxToList();
        if (nextEvent.isEmpty()) {
            log.info("No events to update");
            return;
        }
        OutboxTable outboxTable = nextEvent.getFirst();
        try {
            log.info("Starting retry table outbox");
            eventProducer.sendFileUpdateEvent(fileUpdateTopic, outboxTable.getId(), outboxTable.getPayload());
            outboxTable.setStatus(OutboxStatus.SUCCESS);

        } catch (Exception e) {
            log.error("Retrying table outbox",e);
            OutboxTable failed = new OutboxTable(outboxTable.getId(), null,null, outboxTable.getStatus());
            eventProducer.sendFileUpdateEvent(fileFailedTopic, failed.getId(), failed.getPayload());
        }
    }
}

