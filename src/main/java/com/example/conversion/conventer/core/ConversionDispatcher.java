package com.example.conversion.conventer.core;

import com.example.conversion.conventer.ConverterFile;
import com.example.conversion.dto.ConversionResultDTO;
import com.example.conversion.exceptions.NotConvertingException;
import com.example.conversion.util.FileNameUtil;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Data
public class ConversionDispatcher {
    private List<ConverterFile> converters;

    public ConversionResultDTO conversion(byte[] fileBytes, String fileName) {
        String extension = FileNameUtil.getExtension(fileName);

        ConverterFile converterFile = converters.stream()
                .filter(c -> c.supports(extension))
                .findFirst()
                .orElseThrow(() -> new NotConvertingException("No converter for " + extension));

        return converterFile.convert(fileBytes, fileName);
    }
}
