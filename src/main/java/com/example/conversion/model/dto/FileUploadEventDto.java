package com.example.conversion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadEventDto {
    private UUID fileId;
    private String keyFile;
    private String storageLocation;
    private String targetExtension;
}
