package com.example.conversion.service;

import com.example.conversion.conventer.core.ConversionFacade;
import com.example.conversion.dto.FileUpdateEventDTO;
import com.example.conversion.dto.FileUploadEventDTO;
import com.example.conversion.kafka.ProducerEvent;
import com.example.conversion.minio.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.InputStream;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class FileProcessingService {
    private final ConversionFacade conversionFacade;
    private final MinioService minioService;
    private final ProducerEvent producerEvent;
    @Value("${spring.kafka.topics.file-update}")
    private String fileUpdateTopic;

    public void process(FileUploadEventDTO event) {
        try{
            InputStream file = minioService.getFile(event.getFileName());
            byte[] bytes = file.readAllBytes();

            String convertedFileId = conversionFacade.covertFile(bytes, event.getFileName());
            String filePath = minioService.getFilePath(convertedFileId);

            FileUpdateEventDTO update = new FileUpdateEventDTO(
                    event.getFileId(),
                    event.getFileName(),
                    filePath
            );
            producerEvent.sendFileUpdateEvent(fileUpdateTopic, update);

        } catch (Exception e){
            log.error("Error while listening for file update", e);
            FileUpdateEventDTO failed = new FileUpdateEventDTO(
                    event.getFileId(),
                    event.getFileName(),
                    null
            );
            producerEvent.sendFileUpdateEvent(fileUpdateTopic, failed);
        }
    }
}
