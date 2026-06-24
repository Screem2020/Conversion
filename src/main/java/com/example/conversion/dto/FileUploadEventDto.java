package com.example.conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadEventDto {
    private String fileId;
    private String fileName;
    private String storageLocation;
    private String targetExtension;
}
