package com.example.conversion.conventer.core;

import com.example.conversion.conventer.FileConverter;
import com.example.conversion.model.dto.ConversionResultDto;
import com.example.conversion.exceptions.NotConvertingException;
import com.example.conversion.util.FileNameUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConversionDispatcher {
    private final List<FileConverter> converters;

    public ConversionResultDto conversionFileInDto(byte[] fileBytes, String fileName) {
        log.info("Start conversion file in dto");
        String extension = FileNameUtil.getExtension(fileName);
        FileConverter fileConverter = converters.stream()
                .filter(c -> c.supports(extension))
                .findFirst()
                .orElseThrow(() -> new NotConvertingException("No converter for " + extension));
        return fileConverter.convert(fileBytes, fileName);
    }
}
