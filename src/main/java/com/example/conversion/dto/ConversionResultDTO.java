package com.example.conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConversionResultDTO {
    private byte[] bytes;
    private String fileName;
    private String contentType;
    private String extension;
}
