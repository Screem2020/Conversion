package com.example.conversion.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileToPdfConverter {
    boolean supports(String extension);
    byte[] convert(MultipartFile multipartFile);
}
