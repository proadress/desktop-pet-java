package main.pet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class ResourceImages {
    private ResourceImages() {
    }

    public static URL url(String resourcePath) {
        URL resource = ResourceImages.class.getResource(resourcePath);
        if (resource == null) {
            throw new IllegalArgumentException("Missing image resource: " + resourcePath);
        }
        return resource;
    }

    public static BufferedImage read(String resourcePath) {
        try (InputStream input = ResourceImages.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalArgumentException("Missing image resource: " + resourcePath);
            }
            BufferedImage image = ImageIO.read(input);
            if (image == null) {
                throw new IllegalArgumentException("Unsupported image resource: " + resourcePath);
            }
            return image;
        } catch (IOException exception) {
            throw new IllegalArgumentException("Unable to read image resource: " + resourcePath, exception);
        }
    }

    public static ImageIcon scaled(String resourcePath, int width, int height) {
        Image scaled = read(resourcePath).getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}
