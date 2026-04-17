package com.example.conversion.controller;

import com.example.conversion.conventer.core.ConversionFacade;
import com.example.conversion.dto.FileUploadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/convert")
public class testController {
    private final ConversionFacade conversionFacade;
    @Value("${spring.kafka.topics.file-upload}")

    @PostMapping
    public ResponseEntity<String> convert(@RequestParam("file") MultipartFile file){

        try {
            String fileId = conversionFacade.covertFile(
                    file.getBytes(),
                    file.getName()
            );
            return ResponseEntity.ok(fileId);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error while converting file: " + e.getMessage());
        }
    }
}
