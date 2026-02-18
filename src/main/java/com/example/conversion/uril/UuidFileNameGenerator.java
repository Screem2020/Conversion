package com.example.conversion.uril;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Data
public class UuidFileNameGenerator {
    private static final String DEFAULT_NAME = "File";

        public static String generateUuid(String fileName) {
            int index = fileName.lastIndexOf('.');
            String baseIndex = (index == -1) ? fileName : fileName.substring(0, index);
            if (baseIndex.isEmpty()) {
                baseIndex = DEFAULT_NAME;
            }
            String extension = (index == -1) ? "" : fileName.substring(index);
            return UUID.randomUUID() + "_" + baseIndex + extension;
        }
}
