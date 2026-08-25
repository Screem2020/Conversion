package com.example.conversion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileUpdateEventDto {
    private UUID eventId;
    private String fileId;
    private String payload;
}
