package com.example.conversion.controller;

import com.example.conversion.conventer.core.ConversionFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/convert")
public class TestController {
    private final ConversionFile conversionFile;
    @Value("${spring.kafka.topics.file-upload}")
    private String topicName;

    @PostMapping
    public ResponseEntity<String> convert(@RequestParam("file") MultipartFile file){

        try {
            String fileId = conversionFile.generateFullNameFile(
                    file.getBytes(),
                    file.getOriginalFilename()
            );
            return ResponseEntity.ok(fileId);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error while converting file: " + e.getMessage());
        }
    }
}
