package com.example.conversion.exceptions;

public class NotFoundFileException extends RuntimeException {
    public NotFoundFileException(String message) {
        super(message);
    }
}
