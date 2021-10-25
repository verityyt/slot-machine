package userinterface.components;

import userinterface.Component;
import userinterface.Screen;
import userinterface.WindowHandler;
import userinterface.screens.HomeScreen;
import utils.CustomFont;
import utils.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class GradientBadgeComponent extends Component {

    private final int rectArc;
    private final BufferedImage image;
    private final Color gradientStart;
    private final Color gradientEnd;
    private final int keyFontSize;
    private final int valueFontSize;
    private final String key;
    private final Screen parent;
    public String value;
    private HomeScreen homeScreen = null;
    private boolean isHovered = false;
    private Color valueColor = Color.white;

    public GradientBadgeComponent(Screen parent, int x, int y, int width, int height, String key, String defaultValue, int keyFontSize, int valueFontSize, BufferedImage image, Color gradientStart, Color gradientEnd, int rectArc) {
        super(parent, x, y, width, height);
        this.image = image;
        this.keyFontSize = keyFontSize;
        this.valueFontSize = valueFontSize;
        this.key = key;
        this.value = defaultValue;
        this.gradientStart = gradientStart;
        this.gradientEnd = gradientEnd;
        this.rectArc = rectArc;
        this.parent = parent;
    }

    public void setValue(String newValue, boolean highlightChange, Color highlightColor) {
        value = newValue;

        if (highlightChange) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        valueColor = highlightColor;
                        Thread.sleep(3 * 1000);
                        valueColor = Color.white;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            valueColor = Color.white;
        }

        Logger.ui("Set value of '" + key + "' badge to '" + value + "'!");
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {
        Graphics2D g2d = (Graphics2D) g;

        if (WindowHandler.screen instanceof HomeScreen && homeScreen == null) {
            homeScreen = (HomeScreen) WindowHandler.screen;
        }

        if (WindowHandler.screen instanceof HomeScreen) {
            g2d.setPaint(new GradientPaint(parent.x + x, parent.y + y + (height / 2), gradientStart, parent.x + x + width, parent.y + y + (height / 2), gradientEnd));
            g.fillRoundRect(parent.x + x, parent.y + y, width, height, rectArc, rectArc);

            if (isHovered) {
                // hovered
            }

            int imageHeight = image.getHeight();
            double multiplier = 0.72;
            int calcHeight = (int) (imageHeight * multiplier);

            g2d.drawImage(image, parent.x + x + 107, parent.y + y + (image.getWidth() / 2) - 2, 18, calcHeight, observer);

            g.setColor(valueColor);
            g.setFont(CustomFont.light.deriveFont((float) valueFontSize));

            int stringWidth = g.getFontMetrics().stringWidth(value);
            int stringHeight = g.getFontMetrics().getHeight();

            g.drawString(value, parent.x + x + (width - stringWidth) / 2 - 5, parent.y + y + (height - stringHeight) / 2 + g.getFontMetrics().getAscent() + 6);
            g.setColor(Color.white);
            g.setFont(CustomFont.light.deriveFont((float) keyFontSize));

            int stringWidth2 = g.getFontMetrics().stringWidth(key);

            g.drawString(key, parent.x + x + (width - stringWidth2) / 2 - 5, parent.y + y + (height - stringHeight) / 2 + g.getFontMetrics().getAscent() - 6);

        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getX() > parent.x + x && e.getX() < parent.x + x + width && e.getY() > parent.y + y + 30 && e.getY() < parent.y + y + 30 + height) {
            switch (key) {
                case "SPIN": {
                    if (homeScreen != null && !homeScreen.isSpinOutlineAnimationRunning) {
                        isHovered = true;
                    }
                }
            }
        } else {
            switch (key) {
                case "SPIN": {
                    if (homeScreen != null && !homeScreen.isSpinOutlineAnimationRunning) {
                        isHovered = false;
                    }
                }
            }
        }
    }

}
