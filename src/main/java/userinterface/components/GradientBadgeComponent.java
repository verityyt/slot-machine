package userinterface.components;

import userinterface.Component;
import userinterface.WindowHandler;
import userinterface.screens.HomeScreen;
import utils.CustomFont;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class GradientBadgeComponent extends Component {

    private String key;
    public String value;
    private final int arc;
    private final BufferedImage image;
    private final Color gradientStart;
    private final Color gradientEnd;
    private final int keyFontSize;
    private final int valueFontSize;
    private HomeScreen homeScreen = null;
    private boolean isHovered = false;

    public GradientBadgeComponent(int x, int y, int width, int height, String key, String defaultValue, int keyFontSize, int valueFontSize, BufferedImage image, Color gradientStart, Color gradientEnd, int arc) {
        super(x, y, width, height);
        this.image = image;
        this.keyFontSize = keyFontSize;
        this.valueFontSize = valueFontSize;
        this.key = key;
        this.value = defaultValue;
        this.gradientStart = gradientStart;
        this.gradientEnd = gradientEnd;
        this.arc = arc;
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {
        Graphics2D g2d = (Graphics2D) g;

        if (homeScreen == null) {
            homeScreen = (HomeScreen) WindowHandler.screen;
        }

        if (WindowHandler.screen instanceof HomeScreen) {
            g2d.setPaint(new GradientPaint(x, y + (height / 2), gradientStart, x + width, y + (height / 2), gradientEnd));
            g.fillRoundRect(x, y, width, height, arc, arc);

            if (isHovered) {
                // hovered
            }

            int imageHeight = image.getHeight();
            double multiplier = 0.72;
            int calcHeight = (int) (imageHeight * multiplier);

            g2d.drawImage(image, x + 107, y + (image.getWidth() / 2) - 2, 18, calcHeight, observer);

            g.setColor(Color.white);
            g.setFont(CustomFont.light.deriveFont((float) valueFontSize));

            int stringWidth = g.getFontMetrics().stringWidth(value);
            int stringHeight = g.getFontMetrics().getHeight();

            g.drawString(value, x + (width - stringWidth) / 2 - 5, y + (height - stringHeight) / 2 + g.getFontMetrics().getAscent() + 6);

            g.setFont(CustomFont.light.deriveFont((float) keyFontSize));

            int stringWidth2 = g.getFontMetrics().stringWidth(key);

            g.drawString(key, x + (width - stringWidth2) / 2 - 5, y + (height - stringHeight) / 2 + g.getFontMetrics().getAscent() - 6);

        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getX() > x && e.getX() < x + width && e.getY() > y + 30 && e.getY() < y + 30 + height) {
            switch (key) {
                case "SPIN": {
                    if (!homeScreen.isSpinOutlineAnimationRunning) {
                        isHovered = true;
                    }
                }
            }
        } else {
            switch (key) {
                case "SPIN": {
                    if (!homeScreen.isSpinOutlineAnimationRunning) {
                        isHovered = false;
                    }
                }
            }
        }
    }

}
