package com.example.conversion.exceptions;

public class PdfConvertException extends RuntimeException{
    public PdfConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
