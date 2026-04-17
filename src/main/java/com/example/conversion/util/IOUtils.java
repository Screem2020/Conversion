package com.example.conversion.util;

import java.io.*;

public final class IOUtils {
    private IOUtils() {}

    public static byte[] readEntry(InputStream in) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[8192];
        int len;
        while ((len = in.read(bytes)) != -1) {
            byteArrayOutputStream.write(bytes, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
