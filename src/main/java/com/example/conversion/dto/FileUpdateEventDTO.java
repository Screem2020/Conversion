package com.example.conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileUpdateEventDTO {
    private String fileId;
    private String fileName;
    private String resultPath;
}
