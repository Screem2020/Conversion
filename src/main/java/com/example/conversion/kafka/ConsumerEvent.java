package com.example.conversion.kafka;

import com.example.conversion.dto.FileUpdateEvent;
import com.example.conversion.minio.MinioService;
import com.example.conversion.service.ConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConsumerEvent {
    private final ConversionService covertService;
    private final MinioService minioService;

    @KafkaListener(topics = "file-update-topic", groupId = "conversion-group")
    public void listenFile(FileUpdateEvent fileName) {
        try {
            System.out.println("Listening file: " + fileName);
            InputStream isPdf = minioService.getPdf(fileName.getFileName());
            byte[] fileBytes = isPdf.readAllBytes();
            covertService.covertFile(fileBytes, fileName.getFileName());
        } catch (Exception e) {
            log.error("Error processing file: {}", fileName.getFileName(), e);
        }
    }
}
