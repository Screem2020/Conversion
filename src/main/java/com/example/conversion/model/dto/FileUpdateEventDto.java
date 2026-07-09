package com.example.conversion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class FileUpdateEventDto {
    private UUID fileId;
    private String fileName;
    private String resultPath;
}
