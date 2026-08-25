package com.example.conversion.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Entity
@Data
@Table(name = "inbox")
public class InboxMessage {
    @Id
    private UUID eventId;
    private String fileId;
    private String keyFile;
}
