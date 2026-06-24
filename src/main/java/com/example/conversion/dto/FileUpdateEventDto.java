package com.example.conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class FileUpdateEventDto {
    private String fileId;
    private String fileName;
    private String resultPath;
}
