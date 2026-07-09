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
    private final SchedulerService schedulerService;
    private final ConversionDispatcher conversionDispatcher;
    private final OutboxTableService outboxTableService;

    public void process(FileUploadEventDto event) {
        try{
            InputStream file = minioService.getFile(event.getFileName());
            byte[] bytes = file.readAllBytes();

            String convertedFileId = fileConversionService.generateFullNameFile(bytes, event.getFileName());
            String filePath = minioService.getFilePath(convertedFileId);
            FileUpdateEventDto update = new FileUpdateEventDto(event.getFileId(), event.getFileName(), filePath);

            ConversionResultDto conversion = conversionDispatcher.conversionFileInDto(bytes, convertedFileId);

            OutboxTable outboxTableTask = new OutboxTable(update.getFileId(), update.getFileName(),
                    OutboxEventType.CONVERTER_TASK, OutboxStatus.NEW);
            outboxTableService.save(outboxTableTask);

            schedulerService.publishOutboxEvents();

            minioService.saveFile(conversion, convertedFileId, conversion.contentType());
        } catch (Exception e){
            log.error("Error processing file upload event", e);
        }
    }
}
