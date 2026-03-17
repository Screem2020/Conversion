package com.example.conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileUpdateEvent {
    private String fileId;
    private String fileName;
    private String status;
    private String resultPath;
}
