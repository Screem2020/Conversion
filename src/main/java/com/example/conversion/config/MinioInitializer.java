package com.example.conversion.config;

import com.example.conversion.exceptions.InitMinioException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinioInitializer {

    private final MinioClient minioClient;
    @Value("${minio.bucket-name}")
    private String bucketName;

    @PostConstruct
    public void initBucket() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs
                            .builder()
                            .bucket(bucketName)
                            .build());
            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs
                                .builder()
                                .bucket(bucketName)
                                .build()
                );
            }
        } catch (Exception e) {
            throw new InitMinioException("Error initialization Minio bucket", e);
        }
    }
}
