package com.example.conversion.service;

import com.example.conversion.exceptions.ConvertingFileException;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;

public class MinioService {
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.access-key}")
    private String accessKey;
    @Value("${minio.secret-key}")
    private String secretKey;
    @Value("${minio.bucket-name}")
    private String bucketName;

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    public byte[] savePdf(byte[] pdfByte, String filename) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }

            PutObjectArgs buildObjectArgs = PutObjectArgs.builder()
                    .object(filename)
                    .bucket(bucketName)
                    .stream(new ByteArrayInputStream(pdfByte), pdfByte.length, -1)
                    .contentType("application/pdf")
                    .build();

            try {
                minioClient.putObject(buildObjectArgs);
            } catch (Exception e) {
                throw new ConvertingFileException("Error saving file to Minio");
            }

        } catch  (Exception e) {
            throw new ConvertingFileException("Could not create bucket" + e);
        }
        return pdfByte;
    }
}
