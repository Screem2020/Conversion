package com.example.conversion.conventer.core;

import com.example.conversion.conventer.FileConverter;
import com.example.conversion.dto.ConversionResultDTO;
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
public class ConversionFacade {
    private final List<FileConverter> converters;
    private final MinioService minioService;

    public String covertFile(byte[] bytes, String fileName) {
        String extension = FileNameUtil.getExtension(fileName);
        ConversionResultDTO result = converters.stream()
                .filter(converting -> converting.supports(extension))
                .findFirst()
                .orElseThrow(() -> new NotConvertingException("No result for " + extension))
                .convert(bytes,  fileName);
        String pdfFileId = FileNameUtil.generateNameFileUuid(fileName, result.getExtension());
        minioService.saveFile(result, pdfFileId, result.getContentType());
        return pdfFileId;
    }
}


