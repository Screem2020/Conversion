package com.example.conversion.exceptions;

public class PngConvertException extends RuntimeException{
    public PngConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
