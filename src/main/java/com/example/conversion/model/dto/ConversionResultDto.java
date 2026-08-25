package com.example.conversion.model.dto;

public record ConversionResultDto(
     byte[] bytes,
     String fileName,
     String contentType,
     String extension
) {
}

