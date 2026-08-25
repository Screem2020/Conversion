package com.example.conversion.kafka.consumer;

import com.example.conversion.exceptions.InboxProcessingException;
import com.example.conversion.model.dto.FileUploadEventDto;
import com.example.conversion.model.entity.InboxMessage;
import com.example.conversion.repository.InboxRepository;
import com.example.conversion.service.FileProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileConverterListener {
    private final InboxRepository inboxRepository;
    private final FileProcessingService fileProcessingService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topics.file-upload}")
    public void processFile(String event) {
        try {
            log.info("Received file upload event: {}", event);
            FileUploadEventDto fileUploadEventDto = objectMapper.readValue(event, FileUploadEventDto.class);
            if (inboxRepository.existsByEventId(fileUploadEventDto.getFileId())) {
                log.error("File with id {} already exists", fileUploadEventDto.getFileId());
                throw new InboxProcessingException("file already exists");
            }
            saveInboxMessage(fileUploadEventDto);
            fileProcessingService.process(fileUploadEventDto);
        } catch (Exception e) {
            log.error("Error processing file upload event", e);
        }
    }

    public void saveInboxMessage(FileUploadEventDto fileUploadEventDto) {
        try {
            InboxMessage inboxMessage = new InboxMessage();
            inboxMessage.setEventId(UUID.randomUUID());
            inboxMessage.setFileId(fileUploadEventDto.getFileId().toString());
            inboxMessage.setKeyFile(fileUploadEventDto.getKeyFile());
            log.info("BEFORE SAVE: eventId={}, fileId={}, keyFile={}",
                    inboxMessage.getEventId(),
                    inboxMessage.getFileId(),
                    inboxMessage.getKeyFile());
            inboxRepository.save(inboxMessage);
        } catch (Exception ex) {
            log.warn("Error while saving inbox message", ex);
        }
    }
}
