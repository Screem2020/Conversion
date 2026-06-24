package com.example.conversion.dto;

public record ConversionResultDto(
     byte[] bytes,
     String fileName,
     String contentType,
     String extension) {
}

