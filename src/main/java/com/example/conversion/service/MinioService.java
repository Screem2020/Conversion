package com.example.conversion.service;

import com.example.conversion.model.dto.ConversionResultDto;
import com.example.conversion.exceptions.ConvertingFileException;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {
    @Value("${minio.bucket-name}")
    private String bucketName;
    @Value("${minio.endpoint}")
    private String endpoint;
    private final MinioClient minioClient;

    public void saveFile(ConversionResultDto pdfByte, String fileId, String content) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .object(fileId)
                            .bucket(bucketName)
                            .stream(new ByteArrayInputStream(pdfByte.bytes()), pdfByte.bytes().length, -1)
                            .contentType(content)
                            .build()
            );
            log.info("File save to Minio: {}", fileId);
        } catch (Exception e) {
            log.error("Error saving file to Minio:{}",  fileId, e);
            throw  new ConvertingFileException("Error saving file to Minio", e);
        }
    }

    public InputStream getFile(String filename) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Could not get object from Minio" + filename, e);
        }
    }
}
