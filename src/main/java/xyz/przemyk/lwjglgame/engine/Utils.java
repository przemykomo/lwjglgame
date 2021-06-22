package xyz.przemyk.lwjglgame.engine;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String filename) throws Exception {
        String result;
        InputStream in = Utils.class.getClassLoader().getResourceAsStream(filename);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + filename);
        }

        Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name());
        result = scanner.useDelimiter("\\A").next();
        return result;
    }

    public static byte[] loadImageResource(String filename) throws IOException {
        URL url = Utils.class.getClassLoader().getResource(filename);
        if (url == null) {
            throw new FileNotFoundException("Image resource not found: " + filename);
        }

        // The commented out code loaded image as ABGR, but I need RGBA for opengl
//        BufferedImage bufferedImage = ImageIO.read(url);
//        System.out.println(bufferedImage.getType());
//
//        return ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();


        try (ImageInputStream input = ImageIO.createImageInputStream(new File(url.getFile()))) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);

            if (!readers.hasNext()) {
                throw new IllegalArgumentException("No reader for: " + filename);
            }

            ImageReader reader = readers.next();

            try {
                reader.setInput(input);

                ImageReadParam param = reader.getDefaultReadParam();
                param.setDestinationType(ImageTypeSpecifier.createInterleaved(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{0, 1, 2, 3}, DataBuffer.TYPE_BYTE, true, false));

                BufferedImage image = reader.read(0, param);
                return ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            } finally {
                reader.dispose();
            }
        }
    }
}
