package com.example.conversion.service;

import com.example.conversion.conventer.core.ConversionDispatcher;
import com.example.conversion.conventer.core.ConversionFile;
import com.example.conversion.dto.ConversionResultDto;
import com.example.conversion.dto.FileUpdateEventDto;
import com.example.conversion.dto.FileUploadEventDto;
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
    private final ConversionFile conversionFile;
    private final MinioService minioService;
    private final ProducerEvent producerEvent;
    private final ConversionDispatcher conversionDispatcher;
    @Value("${spring.kafka.topics.file-update}")
    private String fileUpdateTopic;

    public void process(FileUploadEventDto event) {
        try{
            InputStream file = minioService.getFile(event.getFileName());
            byte[] bytes = file.readAllBytes();

            String convertedFileId = conversionFile.generateFullNameFile(bytes, event.getFileName());
            String filePath = minioService.getFilePath(convertedFileId);
            FileUpdateEventDto update = new FileUpdateEventDto(event.getFileId(), event.getFileName(), filePath);

            ConversionResultDto conversion = conversionDispatcher.conversionFileInDto(bytes, convertedFileId);
            minioService.saveFile(conversion, convertedFileId, conversion.contentType());

            producerEvent.sendFileUpdateEvent(fileUpdateTopic, update);

        } catch (Exception e){
            log.error("Error while listening for file update", e);
            FileUpdateEventDto failed = new FileUpdateEventDto(event.getFileId(), event.getFileName(), null);
            producerEvent.sendFileUpdateEvent(fileUpdateTopic, failed);
        }
    }
}
