package com.example.conversion.conventer;

import com.example.conversion.dto.ConversionResultDTO;

public interface ConverterFile {
    boolean supports(String extension);
    ConversionResultDTO convert(byte[] fileByte, String fileName);

}
