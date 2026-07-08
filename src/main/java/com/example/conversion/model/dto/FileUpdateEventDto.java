package com.example.conversion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class FileUpdateEventDto {
    private String fileId;
    private String fileName;
    private String resultPath;
}
