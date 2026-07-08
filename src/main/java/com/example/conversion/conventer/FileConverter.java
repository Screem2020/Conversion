package com.example.conversion.conventer;

import com.example.conversion.model.dto.ConversionResultDto;

public interface FileConverter {
    boolean supports(String extension);
    ConversionResultDto convert(byte[] fileByte, String fileName);

}
