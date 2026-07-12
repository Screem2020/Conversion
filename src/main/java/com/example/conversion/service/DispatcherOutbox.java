package com.example.conversion.service;

import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxEventType;
import com.example.conversion.service.processor.OutboxCompletedProcessor;
import com.example.conversion.service.processor.OutboxDltProcessor;
import com.example.conversion.service.processor.OutboxEventProcessor;
import com.example.conversion.service.processor.OutboxFailedProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class DispatcherOutbox {
    private final Map<OutboxEventType, OutboxEventProcessor> processors;

    public DispatcherOutbox(OutboxCompletedProcessor outboxCompletedProcessor,
                            OutboxFailedProcessor outboxFailedProcessor,
                            OutboxDltProcessor outboxDltProcessor) {
        this.processors = Map.of(OutboxEventType.FILE_COMPLETED, outboxCompletedProcessor,
                                OutboxEventType.FILE_FAILED, outboxFailedProcessor,
                                OutboxEventType.FILE_DLT, outboxDltProcessor);

    }
    public void dispatcher(OutboxTable outboxTable) {
        log.debug("Dispatching outbox events for table {}", outboxTable);
        processors.get(outboxTable.getType()).execute(outboxTable);

    }
}
