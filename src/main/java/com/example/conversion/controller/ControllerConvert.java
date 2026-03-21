package com.example.conversion.controller;

import com.example.conversion.dto.FileUpdateEvent;
import com.example.conversion.dto.FileUploadResponse;
import com.example.conversion.kafka.ProducerEvent;
import com.example.conversion.minio.MinioService;
import com.example.conversion.util.ParsingNameFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class ControllerConvert {
    private final ProducerEvent producerEvent;
    private final MinioService minioService;

    @PostMapping("/convert/v1/")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String fileId = ParsingNameFile.generateNameFileUuid(originalFilename, "raw");
            minioService.saveFile(bytes, fileId, file.getContentType());
            String filePath = minioService.getFilePath(originalFilename);
            producerEvent.sendFileUpdateEvent("files-update-topic", new FileUpdateEvent(fileId, originalFilename, "uploaded", filePath));
            return  ResponseEntity.ok(new FileUploadResponse(fileId, "UPLOADED", filePath));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
