package com.example.conversion.conventer;

import com.example.conversion.dto.ConversionResultDTO;
import com.example.conversion.util.ExtensionNameCorrection;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
public class ConverterToPng implements ConverterFile {
    @Override
    public boolean supports(String extension) {
        return extension.equals("png");
    }

    @Override
    public ConversionResultDTO convert(byte[] fileByte, String fileName) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(fileByte);
            BufferedImage image = ImageIO.read(bis);

            if (image == null) {
                throw new RuntimeException("Invaild PNG file");
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bos);

            byte[] byteArray = bos.toByteArray();

            return new ConversionResultDTO(
                    byteArray,
                    ExtensionNameCorrection.replaceExtension(fileName, "png"),
                    "png",
                    "png/plain"
            );
        }catch (Exception e){
            throw new RuntimeException("JPG conversion failed", e);
        }
    }
}
