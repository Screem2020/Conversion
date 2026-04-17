package com.example.conversion.service;

import com.example.conversion.conventer.core.ConversionFacade;
import com.example.conversion.dto.FileUpdateEvent;
import com.example.conversion.dto.FileUploadEvent;
import com.example.conversion.kafka.ProducerEvent;
import com.example.conversion.minio.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileProcessingService {
    private final ConversionFacade convertService;
    private final MinioService minioService;
    private final ProducerEvent producerEvent;
    @Value("${spring.kafka.topics.file-update}")
    private String fileUpdateTopic;

    public void listenFile(FileUploadEvent event) {
        try{
            InputStream file = minioService.getFile(event.getFileName());
            byte[] bytes = file.readAllBytes();

            String convertedFileId = convertService.covertFile(bytes, event.getFileName());
            String filePath = minioService.getFilePath(convertedFileId);

            FileUpdateEvent update = new FileUpdateEvent(
                    event.getFileId(),
                    event.getFileName(),
                    filePath
            );
            producerEvent.sendFileUpdateEvent(fileUpdateTopic, update);

        } catch (Exception e){
            log.error("Error while listening for file update", e);
            FileUpdateEvent failed = new FileUpdateEvent(
                    event.getFileId(),
                    event.getFileName(),
                    null
            );
            producerEvent.sendFileUpdateEvent(fileUpdateTopic, failed);
        }
    }
}
