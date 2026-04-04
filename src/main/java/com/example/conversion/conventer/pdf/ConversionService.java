package com.example.conversion.conventer.pdf;

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
public class ConversionService {
    private final List<PdfConverter> converters;
    private final MinioService minioService;

    public String covertFile(byte[] bytes, String fileName) {
        String extension = FileNameUtil.getExtension(fileName);
        byte[] convert = converters.stream()
                .filter(converting -> converting.supports(extension))
                .findFirst()
                .orElseThrow(() -> new NotConvertingException("No convert for " + extension))
                .convert(bytes);
        String pdfFileId = FileNameUtil.generateNameFileUuid(fileName, "pdf");
        minioService.saveFile(convert, pdfFileId, "application/pdf");
        return pdfFileId;
    }
}


