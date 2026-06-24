package com.example.conversion.conventer.core;

import com.example.conversion.conventer.FileConverter;
import com.example.conversion.dto.ConversionResultDto;
import com.example.conversion.exceptions.NotConvertingException;
import com.example.conversion.minio.MinioService;
import com.example.conversion.util.FileNameUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Data
public class ConversionFile {
    private final List<FileConverter> converters;

    public String generateFullNameFile(byte[] bytes, String fileName) {
        String extension = FileNameUtil.getExtension(fileName);

        ConversionResultDto result = converters.stream()
                .filter(converting -> converting.supports(extension))
                .findFirst()
                .orElseThrow(() -> new NotConvertingException("No result for " + extension))
                .convert(bytes,  fileName);
        return FileNameUtil.generateNameFileUuid(fileName, result.extension());
//        minioService.saveFile(result, pdfFileId, result.contentType());
    }
}


