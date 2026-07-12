package com.example.conversion.kafka.consumer;

import com.example.conversion.model.dto.FileUploadEventDto;
import com.example.conversion.model.entity.InboxMessage;
import com.example.conversion.repository.InboxRepository;
import com.example.conversion.service.FileProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class FileConverterListener {
    private final InboxRepository inboxRepository;
    private final FileProcessingService fileProcessingService;

    @RetryableTopic(
            attempts = "4",
            backOff = @BackOff(delay = 5000)
    )
    @KafkaListener(topics = "${spring.kafka.topics.file-upload}")
    public void processFile(FileUploadEventDto event) {
        log.info("Received file upload event: {}", event);
        boolean alreadyProcessed = inboxRepository.existsByEventId(event.getFileId());
        if (alreadyProcessed) {
            return;
        }
        InboxMessage inboxMessage = new InboxMessage();
        inboxMessage.setEventId(event.getFileId());
        inboxMessage.setTimestamp(LocalDateTime.now());
        inboxRepository.save(inboxMessage);

        fileProcessingService.process(event);
    }

}
