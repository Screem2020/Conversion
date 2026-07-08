package com.example.conversion.model.enums;

public enum OutboxEventType {
    CONVERTER_TASK,
    ADD;

    public static OutboxEventType fromStringType(String type) {
        for (OutboxEventType outboxEventType : values()) {
            if (outboxEventType.name().equalsIgnoreCase(type)) {
                return outboxEventType;
            }
        }
        throw new IllegalArgumentException(String.format("Unknown OutboxEventType %s", type));
    }
}

