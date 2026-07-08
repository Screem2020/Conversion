package com.example.conversion.model.enums;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum OutboxStatus {
    SUCCESS, IN_PROGRESS, FILED;

    public static OutboxStatus fromStringStatus(String status) {
        for (OutboxStatus outboxStatus : values()) {
            if (outboxStatus.name().equalsIgnoreCase(status)) {
                return outboxStatus;
            }
        }
        throw new IllegalArgumentException("Invalid outbox status: " + status);
    }
}
