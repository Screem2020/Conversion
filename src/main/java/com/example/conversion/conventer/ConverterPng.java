package com.example.conversion.conventer;

import com.example.conversion.model.dto.ConversionResultDto;
import com.example.conversion.exceptions.FileNotFoundException;
import com.example.conversion.exceptions.PngConvertException;
import com.example.conversion.util.ExtensionNameCorrection;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
public class ConverterPng implements FileConverter {
    @Override
    public boolean supports(String extension) {
        return extension.equals("png");
    }

    @Override
    public ConversionResultDto convert(byte[] fileByte, String fileName) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(fileByte);
            BufferedImage image = ImageIO.read(bis);

            if (image == null) {
                throw new FileNotFoundException("Invalid PNG file");
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bos);

            byte[] byteArray = bos.toByteArray();

            return new ConversionResultDto(
                    byteArray,
                    ExtensionNameCorrection.replaceExtension(fileName, "png"),
                    "png",
                    "png/plain"
            );
        }catch (Exception e){
            throw new PngConvertException("JPG conversion failed", e);
        }
    }
}
