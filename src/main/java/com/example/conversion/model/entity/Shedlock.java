package com.example.conversion.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@Entity
@Table(name = "shedlock")
public class Shedlock {

    @Id
    private String name;
    private Instant lockUntil;
    private Instant lockedAt;
    private String lockedBy;
}
