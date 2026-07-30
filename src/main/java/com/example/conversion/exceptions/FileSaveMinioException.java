package com.example.conversion.exceptions;

public class FileSaveMinioException extends RuntimeException {
    public FileSaveMinioException(String message) {
        super(message);
    }

    public FileSaveMinioException(String message, Throwable cause) {
        super(message, cause);
    }
}
