package com.example.conversion.service;

import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxEventType;
import com.example.conversion.model.enums.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerJobService {
    private final OutboxManager outboxManager;
    private final DispatcherOutbox dispatcherOutbox;
    private final LifePolicyService lifePolicyService;

    @Transactional
    @Scheduled(fixedRateString = "${scheduler.fixed-rate}")
    @SchedulerLock(
            name = "publishOutboxEvents",
            lockAtLeastFor = "PT1M",
            lockAtMostFor = "PT10M")
    public void publishOutboxEvents() {
        List<OutboxTable> nextEvent = outboxManager.eventOutboxToList();
        if (nextEvent.isEmpty()) {
            log.info("No events " +
                    "to update");
            return;
        }
        for (OutboxTable outboxTable : nextEvent) {
            try {
                if (lifePolicyService.processPolicy(outboxTable)){
                    log.info("File send to dlt");
                    outboxTable.setType(OutboxEventType.FILE_DLT);
                    outboxTable.setStatus(OutboxStatus.FAILED);
                } else {
                    lifePolicyService.registerAttempt(outboxTable);
                    outboxTable.setStatus(OutboxStatus.SUCCESS);
                }
                dispatcherOutbox.dispatcher(outboxTable);
            } catch (Exception e) {
                outboxTable.setStatus(OutboxStatus.NEW);
                log.error("Retrying table outbox", e);
            }
        }
    }
}

