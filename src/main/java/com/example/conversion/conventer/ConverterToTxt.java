package com.example.conversion.conventer;

import com.example.conversion.dto.ConversionResultDTO;
import com.example.conversion.util.ExtensionNameCorrection;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ConverterToTxt implements ConverterFile {
    @Override
    public boolean supports(String extension) {
        return extension.equalsIgnoreCase("txt") ;
    }

    @Override
    public ConversionResultDTO convert(byte[] fileByte, String fileName) {
        String text = new String(fileByte, StandardCharsets.UTF_8);
        String normalizedText = text.trim();

        if (!normalizedText.endsWith("\r\n")) {
            normalizedText+="\r\n";
        }

        byte[] bytes = normalizedText.getBytes(StandardCharsets.UTF_8);
        return new ConversionResultDTO(
                bytes,
                ExtensionNameCorrection.replaceExtension(fileName, "txt"),
                "txt",
                "txt/plain"
        );
    }
}
