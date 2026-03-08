package com.example.conversion.service;

public interface FileToPdfConverter {
    boolean supports(String extension);
    byte[] convert(byte[] fileByte);

}
