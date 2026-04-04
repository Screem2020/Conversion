package com.example.conversion.conventer.pdf;

public interface PdfConverter {
    boolean supports(String extension);
    byte[] convert(byte[] fileByte);

}
