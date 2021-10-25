package utils;

import java.awt.*;

public class ColorUtils {

    public static final Color red = Color.decode("#E74C3C");
    public static final Color blue = Color.decode("#3498DB");
    public static final Color green = Color.decode("#2ECC71");

    public static String getHexFromRGB(int rgb) {
        int firstWheelR = (rgb & 0x00ff0000) >> 16;
        int firstWheelG = (rgb & 0x0000ff00) >> 8;
        int firstWheelB = rgb & 0x000000ff;
        return String.format("#%02x%02x%02x", firstWheelR, firstWheelG, firstWheelB);
    }

}
