package com.example.conversion.model.dto;

import com.example.conversion.model.enums.OutboxEventType;

public record ConversionResultDto(
     byte[] bytes,
     String fileName,
     String contentType,
     String extension
) {
}

