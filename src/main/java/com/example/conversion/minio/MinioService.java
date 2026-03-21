package com.example.conversion.minio;

import com.example.conversion.exceptions.ConvertingFileException;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
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
//        try {
//            boolean bucketExists = minioClient.bucketExists(
//                    BucketExistsArgs
//                            .builder()
//                            .bucket(bucketName)
//                            .build());
//            if (!bucketExists) {
//                minioClient.makeBucket(
//                        MakeBucketArgs
//                                .builder()
//                                .bucket(bucketName)
//                                .build()
//                );
//                log.info("Bucket created: {}", bucketName);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Error initialization Minio bucket", e);
//        }
    }

    public void saveFile(byte[] pdfByte, String filename, String content) {
        try {

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .object(filename)
                            .bucket(bucketName)
                            .stream(new ByteArrayInputStream(pdfByte), pdfByte.length, -1)
                            .contentType(content)
                            .build()
            );
            log.info("File save to Minio: {}", filename);


        } catch (Exception e) {
            log.error("Error saving file to Minio:{}",  filename, e);
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
    public String getFilePath(String fileId) {
        return bucketName + "/" + fileId;
    }
}
