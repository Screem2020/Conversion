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
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class FileProcessingService {
    private final FileConversionService fileConversionService;
    private final MinioService minioService;
    private final ConversionDispatcher conversionDispatcher;
    private final OutboxManager outboxManager;
    private final ObjectMapper objectMapper;

    public void process(FileUploadEventDto event) {
        try (InputStream file = minioService.getFile(event.getKeyFile())) {
            byte[] bytes = file.readAllBytes();
            log.info(
                    "Downloaded file: {}, size: {} bytes",
                    event.getKeyFile(),
                    bytes.length
            );
            String convertedFileId = fileConversionService.generateFullNameFile(bytes, event.getKeyFile());
            FileUpdateEventDto update = new FileUpdateEventDto(event.getFileId(), event.getKeyFile());

            ConversionResultDto conversion = conversionDispatcher.conversionFileInDto(bytes, convertedFileId);

            minioService.saveFile(conversion, convertedFileId, conversion.contentType());

            String payload = objectMapper.writeValueAsString(update);
            OutboxTable completedEvent = new OutboxTable(
                    null,
                    update.getFileId(),
                    payload,
                    null,
                    0,
                    OutboxEventType.FILE_COMPLETED,
                    OutboxStatus.NEW);

            outboxManager.save(completedEvent);

        } catch (Exception e) {
            log.error("Error processing file upload event", e);
            String payload = objectMapper.writeValueAsString(event);
            OutboxTable failedEvent = new OutboxTable(null, event.getFileId(), payload, null, 0,
                    OutboxEventType.FILE_FAILED, OutboxStatus.NEW);
            outboxManager.save(failedEvent);
        }

    }
}
