package com.example.conversion.model.entity;

import com.example.conversion.model.enums.OutboxEventType;
import com.example.conversion.model.enums.OutboxStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "outbox")
public class OutboxTable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String payload;
    @Enumerated(EnumType.STRING)
    private OutboxEventType type;
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;
}
