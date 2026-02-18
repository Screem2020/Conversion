package com.example.conversion.service;

import com.example.conversion.exceptions.ConvertingFileException;
import com.example.conversion.exceptions.NotFoundFileException;
import com.example.conversion.uril.ParsingNameFile;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RequiredArgsConstructor
@Service
@Data
public class ConversionService {

    private final List<FileToPdfConverter> converters;
    private final MinioService minioService;


    public byte[] covertFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = ParsingNameFile.getExtension(originalFilename);
        byte[] convertPdfFile = converters.stream()
                .filter(convert -> convert.supports(extension))
                .map(convert -> {
                    try {
                        return convert.convert(file);
                    } catch (Exception e) {
                        throw new ConvertingFileException(originalFilename, e);
                    }
                })
                .findFirst()
                .orElseThrow(() -> new NotFoundFileException("File not supported"));
        String convertPdfFileWithUuid = ParsingNameFile.generateNameFileUuid(file.getOriginalFilename(), extension);
        return minioService.savePdf(convertPdfFile, convertPdfFileWithUuid);
    }
}


