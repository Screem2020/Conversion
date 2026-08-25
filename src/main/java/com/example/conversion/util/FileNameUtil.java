package com.example.conversion.util;

import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class FileNameUtil {

    public static final String DEFAULT_NAME = "file";

    public static String getBaseName(String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            return DEFAULT_NAME;
        }
        int indexPoint = originalFileName.lastIndexOf('.');
        if (indexPoint <= 0) {
            return DEFAULT_NAME;
        }
        return originalFileName.substring(0, indexPoint);
    }

    public static String getExtension(String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            return "";
        }
        int indexPoint = originalFileName.lastIndexOf('.');
        if (indexPoint == -1 || indexPoint == originalFileName.length() - 1) {
            return "";
        }
        return originalFileName.substring(indexPoint + 1).toLowerCase();
    }

    public static String replaceExtension(String originalFileName, String newExtension) {
        String base = getBaseName(originalFileName);
        if (newExtension == null || newExtension.isBlank()) {
            newExtension = "";
        } else if(!newExtension.startsWith(".")) {
            newExtension = "." + newExtension;
        }
        return base + newExtension;
    }

    public static String generateNameFileUuid(String fileName, String extension) {
        return UUID.randomUUID() + "_" + replaceExtension(fileName, extension);
    }
}
