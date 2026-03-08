package com.example.conversion.service;

import com.example.conversion.exceptions.ConvertingFileException;
import com.example.conversion.exceptions.NotFoundFileException;
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


    public byte[] covertFile(byte[] bytes, String fileName) {
        String extension = ParsingNameFile.getExtension(fileName);
        byte[] convertPdfFile = converters.stream()
                .filter(convert -> convert.supports(extension))
                .map(convert -> {
                    try {
                        return convert.convert(bytes);
                    } catch (Exception e) {
                        throw new ConvertingFileException(fileName, e);
                    }
                })
                .findFirst()
                .orElseThrow(() -> new NotFoundFileException("File not supported"));
        String convertPdfFileWithUuid = ParsingNameFile.generateNameFileUuid(fileName, extension);
        return minioService.savePdf(convertPdfFile, convertPdfFileWithUuid);
    }
}


