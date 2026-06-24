package com.example.conversion.conventer.core;

import com.example.conversion.conventer.FileConverter;
import com.example.conversion.dto.ConversionResultDto;
import com.example.conversion.exceptions.NotConvertingException;
import com.example.conversion.util.FileNameUtil;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Data
public class ConversionDispatcher {
    private List<FileConverter> converters;

    public ConversionResultDto conversionFileInDto(byte[] fileBytes, String fileName) {
        String extension = FileNameUtil.getExtension(fileName);

        FileConverter fileConverter = converters.stream()
                .filter(c -> c.supports(extension))
                .findFirst()
                .orElseThrow(() -> new NotConvertingException("No converter for " + extension));

        return fileConverter.convert(fileBytes, fileName);
    }
}
