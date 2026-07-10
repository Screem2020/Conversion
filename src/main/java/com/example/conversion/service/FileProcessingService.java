package com.example.conversion.service;

import com.example.conversion.conventer.core.ConversionDispatcher;
import com.example.conversion.conventer.core.FileConversionService;
import com.example.conversion.model.dto.ConversionResultDto;
import com.example.conversion.model.dto.FileUpdateEventDto;
import com.example.conversion.model.dto.FileUploadEventDto;
import com.example.conversion.model.entity.OutboxTable;
import com.example.conversion.model.enums.OutboxEventType;
import com.example.conversion.model.enums.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class FileProcessingService {
    private final FileConversionService fileConversionService;
    private final MinioService minioService;
    private final ConversionDispatcher conversionDispatcher;
    private final OutboxTableService outboxTableService;

    public void process(FileUploadEventDto event) {
        FileUpdateEventDto update = null;
        try (InputStream file = minioService.getFile(event.getFileName())) {
            byte[] bytes = file.readAllBytes();

            String convertedFileId = fileConversionService.generateFullNameFile(bytes, event.getFileName());
            String filePath = minioService.getFilePath(convertedFileId);
            update = new FileUpdateEventDto(event.getFileId(), event.getFileName(), filePath);

            OutboxTable completedEvent = new OutboxTable(update.getFileId(), update.getFileName(),
                    OutboxEventType.FILE_CONVERTER, OutboxStatus.NEW);
            outboxTableService.save(completedEvent);

            ConversionResultDto conversion = conversionDispatcher.conversionFileInDto(bytes, convertedFileId);

            minioService.saveFile(conversion, convertedFileId, conversion.contentType());

        } catch (Exception e) {
            if (update != null) {
                OutboxTable failedEvent = new OutboxTable(update.getFileId(), update.getFileName(),
                        OutboxEventType.FILE_FAILED, OutboxStatus.NEW);
            log.info("Sending failed event for {}", failedEvent.getId());
            outboxTableService.save(failedEvent);
            log.error("Error processing file upload event", e);
            }
        }
    }
}
