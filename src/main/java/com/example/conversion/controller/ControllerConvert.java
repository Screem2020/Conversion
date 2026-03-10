package com.example.conversion.controller;

import com.example.conversion.dto.FileUpdateEvent;
import com.example.conversion.kafka.ProducerEvent;
import com.example.conversion.service.ConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class ControllerConvert {
    private final ConversionService conversionService;
    private final ProducerEvent producerEvent;
    private final String topic = "files-update-topic";

    @PostMapping("/convert/v1/")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try {
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            conversionService.covertFile(bytes, originalFilename);
            producerEvent.sendFileUpdateEvent(topic, new FileUpdateEvent(originalFilename, "uploaded"));
            return  ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
