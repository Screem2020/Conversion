package com.example.conversion.conventer;

import com.example.conversion.model.dto.ConversionResultDto;
import com.example.conversion.exceptions.FileNotFoundException;
import com.example.conversion.exceptions.PdfConvertException;
import com.example.conversion.util.ExtensionNameCorrection;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
public class ConverterPdf implements FileConverter {

    @Override
    public boolean supports(String extension) {
        return extension.equals("pdf");
    }

    @Override
    public ConversionResultDto convert(byte[] fileByte, String fileName) {
        try{
            ByteArrayInputStream bais = new ByteArrayInputStream(fileByte);
            BufferedImage image = ImageIO.read(bais);

            if (image == null) {
                throw new FileNotFoundException("File is not a valid PNG file");
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bos);

            byte[] byteArray = bos.toByteArray();

            return new ConversionResultDto(
                    byteArray,
                    ExtensionNameCorrection.replaceExtension(fileName, "png"),
                    "image/png",
                    "png"
            );
        } catch (Exception e) {
            throw new PdfConvertException("PNG conversion failed", e);
        }
    }
}
