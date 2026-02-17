package com.example.conversion.service;

import com.example.conversion.exceptions.ConvertingFileException;
import com.example.conversion.exceptions.NotFoundFileException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Data
public class ConversionService {

    private final List<FileToPdfConverter> converters;


    public byte[] convert(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFilename)
                .substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        return converters.stream()
                .filter(convert -> convert.supports(extension))
                .map(convert -> {
                    try {
                       return convert.convert(file);
                    } catch (Exception e) {
                        throw new ConvertingFileException(originalFilename, e);
                    }
                })
                .findFirst()
                .orElseThrow(() -> new NotFoundFileException("File not supported"));
    }
}


