package com.example.conversion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class ConversionResultDTO {
    private byte[] bytes;
    private String contentType;
    private String extension;
}
