package com.example.conversion.conventer;

import com.example.conversion.conventer.core.ConversionDispatcher;
import com.example.conversion.model.dto.ConversionResultDto;
import com.example.conversion.exceptions.ZipConverterException;
import com.example.conversion.util.ExtensionNameCorrection;
import com.example.conversion.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
public class ArchiveProcessorZip implements FileConverter {
    private final ConversionDispatcher dispatcher;


    @Override
    public boolean supports(String extension) {
        return extension.equals("zip");
    }

    @Override
    public ConversionResultDto convert(byte[] fileByte, String fileName) {
        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(fileByte);
                ZipInputStream zipInputStream = new ZipInputStream(inputStream);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        ) {
            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                byte[] fileBytes = IOUtils.readEntry(zipInputStream);
                ConversionResultDto result = dispatcher.conversionFileInDto(fileBytes, entry.getName());

                ZipEntry zipEntry = new ZipEntry(result.fileName());
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(result.bytes());
                zipOutputStream.closeEntry();

            }
            return new ConversionResultDto(
                    outputStream.toByteArray(),
                    ExtensionNameCorrection.replaceExtension(fileName, "zip"),
                    "application/zip",
                    "zip"
            );
        } catch (Exception e) {
            throw new ZipConverterException("ZIP conversion failed for file " + fileName, e);
        }
    }
}
