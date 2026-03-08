package com.example.conversion.minio;

import com.example.conversion.exceptions.ConvertingFileException;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
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

        } catch (Exception e) {
            throw new ConvertingFileException("Could not create bucket" + e);
        }
        return pdfByte;
    }

    public InputStream getPdf(String filename) {
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
