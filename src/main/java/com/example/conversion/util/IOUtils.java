package com.example.conversion.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
@Slf4j
@NoArgsConstructor
public final class IOUtils {
    public static byte[] readEntry(InputStream in) throws IOException {
        log.debug("Reading entry from input stream");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[8192];
        int len;
        while ((len = in.read(bytes)) != -1) {
            byteArrayOutputStream.write(bytes, 0, len);
        }
        log.debug("Finished reading entry from input stream");
        return byteArrayOutputStream.toByteArray();
    }
}
