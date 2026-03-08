package com.example.conversion.dto;

import lombok.Data;

@Data
public class FileUpdateEvent {
    private String fileName;
    private String status;
}
