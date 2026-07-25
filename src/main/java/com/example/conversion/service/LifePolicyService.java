package com.example.conversion.service;

import com.example.conversion.model.entity.OutboxTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class LifePolicyService {
    @Value("${outbox.retry.timeout}")
    private Duration timeout;
    @Value("${outbox.retry.max-attempts}")
    private Integer maxAttempts;

    private boolean checkLifeTime(OutboxTable outboxTable) {
        Duration between = Duration.between(outboxTable.getLifeTime(), Instant.now());
        return between.compareTo(timeout) >= 0;
    }

    private boolean checkLifeAttempts(OutboxTable outboxTable) {
        return maxAttempts < outboxTable.getAttempts();
    }

    public boolean processPolicy(OutboxTable outboxTable) {
        return checkLifeAttempts(outboxTable) && checkLifeTime(outboxTable);
    }

    public void registerAttempt(OutboxTable outboxTable) {
        log.info("Registering attempt for outbox {}", outboxTable.getOutboxId());
        if (outboxTable.getLifeTime() == null) {
            outboxTable.setLifeTime(Instant.now());
        }
        outboxTable.setAttempts(outboxTable.getAttempts() + 1);
    }
}
