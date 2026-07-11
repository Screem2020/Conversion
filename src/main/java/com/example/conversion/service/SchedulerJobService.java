package com.example.conversion.service;

import com.example.conversion.exceptions.ConvertingFileException;
import com.example.conversion.model.entity.OutboxTable;
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

    @Transactional
    @Scheduled(fixedRate = 5000)
    @SchedulerLock(
            name = "publishOutboxEvents",
            lockAtLeastFor = "PT1M",
            lockAtMostFor = "PT10M")


    public void publishOutboxEvents() {
        List<OutboxTable> nextEvent = outboxManager.eventOutboxToList();
        if (nextEvent.isEmpty()) {
            log.info("No events to update");
            return;
        }

        for (OutboxTable outboxTable : nextEvent) {
            try {
                dispatcherOutbox.dispatcher(outboxTable);
                outboxTable.setStatus(OutboxStatus.SUCCESS);
            } catch (Exception e) {
                //TODO: разработать механизм для ограничения безконечных повторений
                outboxTable.setStatus(OutboxStatus.NEW);
                log.error("Retrying table outbox", e);
            }
        }
    }
}

