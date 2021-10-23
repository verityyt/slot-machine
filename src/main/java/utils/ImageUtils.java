package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static BufferedImage resize(BufferedImage img, int width, int height) {
        Image temp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();

        return image;
    }

}