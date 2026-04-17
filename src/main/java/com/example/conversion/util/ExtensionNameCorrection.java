package com.example.conversion.util;

public final class ExtensionNameCorrection {

    private ExtensionNameCorrection() {}

    public static String replaceExtension(String fileName, String extension) {
        if (fileName == null || fileName.isBlank()) {
            return "file." + extension;
        }

        int dot = fileName.lastIndexOf('.');

        String baseName = (dot == -1)
                ? fileName
                : fileName.substring(0, dot);

        return baseName + "." + extension;
    }
}
