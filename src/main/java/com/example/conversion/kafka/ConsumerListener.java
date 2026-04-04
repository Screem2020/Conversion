package com.example.conversion.kafka;

import com.example.conversion.dto.FileUploadEvent;
import com.example.conversion.service.FileProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerListener {
    private final FileProcessingService fileProcessingService;

    @KafkaListener(topics = "${spring.kafka.topics.file-upload}")
    public void listenFile(FileUploadEvent event) {
        log.info("Received file upload event: {}", event);
        fileProcessingService.listenFile(event);
    }

}
