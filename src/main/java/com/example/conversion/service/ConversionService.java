package com.example.conversion.service;

import com.example.conversion.exceptions.NotConvertingException;
import com.example.conversion.minio.MinioService;
import com.example.conversion.util.ParsingNameFile;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Data
public class ConversionService {
    private final List<FileToPdfConverter> converters;
    private final MinioService minioService;

    public String covertFile(byte[] bytes, String fileName) {
        String extension = ParsingNameFile.getExtension(fileName);
        byte[] convert = converters.stream()
                .filter(converting -> converting.supports(extension))
                .findFirst()
                .orElseThrow(() -> new NotConvertingException("No convert for " + extension))
                .convert(bytes);
        String pdfFileId = ParsingNameFile.generateNameFileUuid(fileName, "pdf");
        minioService.saveFile(convert, pdfFileId, "application/pdf");
        return pdfFileId;
    }
}


