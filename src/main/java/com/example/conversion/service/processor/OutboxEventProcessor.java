package com.example.conversion.service.processor;

import com.example.conversion.model.entity.OutboxTable;

public interface OutboxEventProcessor {
    void execute(OutboxTable outboxTable);
}