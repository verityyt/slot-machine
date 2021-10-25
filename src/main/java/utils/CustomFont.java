package utils;

import java.awt.*;
import java.io.File;

public class CustomFont {

    private static final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    public static Font regular;
    public static Font italic;
    public static Font light;
    public static Font medium;
    public static Font bold;

    public static void registerAll() {
        registerRegular();
        registerItalic();
        registerLight();
        registerMedium();
        registerBold();

        Logger.ui("Registered all fonts!");
    }

    private static void registerRegular() {
        try {
            regular = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/Rubik-Regular.ttf"));
            ge.registerFont(regular);
        } catch (Exception e) {
            Logger.warn("Unable to create font 'regular' (" + e.getMessage() + ")");
        }
    }

    private static void registerItalic() {
        try {
            italic = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/Rubik-Italic.ttf"));
            ge.registerFont(italic);
        } catch (Exception e) {
            Logger.warn("Unable to create font 'italic' (" + e.getMessage() + ")");
        }
    }

    private static void registerLight() {
        try {
            light = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/Rubik-Light.ttf"));
            ge.registerFont(light);
        } catch (Exception e) {
            Logger.warn("Unable to create font 'light' (" + e.getMessage() + ")");
        }
    }

    private static void registerMedium() {
        try {
            medium = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/Rubik-Medium.ttf"));
            ge.registerFont(medium);
        } catch (Exception e) {
            Logger.warn("Unable to create font 'medium' (" + e.getMessage() + ")");
        }
    }

    private static void registerBold() {
        try {
            bold = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/Rubik-Bold.ttf"));
            ge.registerFont(bold);
        } catch (Exception e) {
            Logger.warn("Unable to create font 'bold' (" + e.getMessage() + ")");
        }
    }

}
