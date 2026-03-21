package com.example.conversion.kafka;

import com.example.conversion.dto.FileUpdateEvent;
import com.example.conversion.dto.FileUploadEvent;
import com.example.conversion.minio.MinioService;
import com.example.conversion.service.ConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConsumerEvent {
    private final ConversionService convertService;
    private final MinioService minioService;
    private final ProducerEvent producerEvent;
    @Value("${kafka.topics.file-update}")
    private String fileUpdateTopic;

    @KafkaListener(topics = "${kafka.topics.file-upload}")
    public void listenFile(FileUploadEvent event) {
        try{
            InputStream file = minioService.getFile(event.getFileName());
            byte[] bytes = file.readAllBytes();

            String pdfFileId = convertService.covertFile(bytes, event.getFileName());
            String filePath = minioService.getFilePath(pdfFileId);

            FileUpdateEvent update = new FileUpdateEvent(
                    event.getFileId(),
                    event.getFileName(),
                    "CONVERTED",
                    filePath
            );
            producerEvent.sendFileUpdateEvent(fileUpdateTopic, update);

        } catch (Exception e){
            log.error("Error while listening for file update", e);
            FileUpdateEvent failed = new FileUpdateEvent(
                    event.getFileId(),
                    event.getFileName(),
                    "FAILED",
                    null
            );
            producerEvent.sendFileUpdateEvent(fileUpdateTopic, failed);
        }
    }
}
