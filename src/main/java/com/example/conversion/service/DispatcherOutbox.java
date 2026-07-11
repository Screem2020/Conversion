package com.example.conversion.service;

import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxEventType;
import com.example.conversion.service.processor.OutboxCompletedProcessor;
import com.example.conversion.service.processor.OutboxEventProcessor;
import com.example.conversion.service.processor.OutboxFailedProcessor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DispatcherOutbox {
    private final Map<OutboxEventType, OutboxEventProcessor> processors;

    public DispatcherOutbox(OutboxCompletedProcessor outboxCompletedProcessor,
                            OutboxFailedProcessor outboxFailedProcessor) {
        this.processors = Map.of(OutboxEventType.FILE_COMPLETED, outboxCompletedProcessor,
                                OutboxEventType.FILE_FAILED, outboxFailedProcessor);

    }

    public void dispatcher(OutboxTable outboxTable) {
        processors.get(outboxTable.getType()).execute(outboxTable);

    }
}
