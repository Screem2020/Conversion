package com.example.conversion.conventer.core;

import com.example.conversion.conventer.FileConverter;
import com.example.conversion.model.dto.ConversionResultDto;
import com.example.conversion.exceptions.NotConvertingException;
import com.example.conversion.util.FileNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileConversionService {
    private final List<FileConverter> converters;

    public String generateFullNameFile(byte[] bytes, String fileName) {
        String extension = FileNameUtil.getExtension(fileName);
        log.info("fileName='{}', extension='{}'", fileName, extension);
        ConversionResultDto result = converters.stream()
                .filter(converting -> converting.supports(extension))
                .findFirst()
                .orElseThrow(() -> new NotConvertingException("No result for " + extension))
                .convert(bytes,  fileName);
        return FileNameUtil.generateNameFileUuid(fileName, result.extension());
    }
}


