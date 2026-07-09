package com.example.conversion.model.entity;

import com.example.conversion.model.enums.OutboxEventType;
import com.example.conversion.model.enums.OutboxStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "outbox_table")
public class OutboxTable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(columnDefinition = "jsonb")
    private String payload;
    @Enumerated(EnumType.STRING)
    private OutboxEventType type;
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    public OutboxTable(UUID id, String payload) {
        this.id = id;
        this.payload = payload;
    }
}
