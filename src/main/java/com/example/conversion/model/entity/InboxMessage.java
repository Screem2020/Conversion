package com.example.conversion.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "inbox_messages")
public class InboxMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID eventId;
    private LocalDateTime timestamp;
}
