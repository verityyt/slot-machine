package userinterface.components;

import userinterface.Component;
import userinterface.WindowHandler;
import userinterface.screens.HomeScreen;
import utils.CustomFont;
import utils.ImageUtils;
import utils.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class GradientButtonComponent extends Component {

    public static String text;
    private final int arc;
    private final BufferedImage image;
    private final Color gradientStart;
    private final Color gradientEnd;
    private final int fontSize;
    public boolean isRotating = false;
    public double rotation = 0.0;
    private HomeScreen homeScreen = null;
    private boolean isHovered = false;

    public GradientButtonComponent(int x, int y, int width, int height, String text, int fontSize, BufferedImage image, Color gradientStart, Color gradientEnd, int arc) {
        super(x, y, width, height);
        this.image = ImageUtils.resize(image, 25, 25);
        this.fontSize = fontSize;
        GradientButtonComponent.text = text;
        this.gradientStart = gradientStart;
        this.gradientEnd = gradientEnd;
        this.arc = arc;
    }



    public void rotateImage() {
        if (!isRotating) {
            isRotating = true;

            new Thread() { // Spin image rotation animation (Runs once per spin-start)
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(10);
                            rotation += 0.1;
                        } catch (Exception e) {
                            Logger.error(e.getMessage());
                        }

                        if (!homeScreen.currentlySpinning && (Math.toDegrees(rotation) % 360) < 10) {
                            rotation = 0;
                            isRotating = false;
                            break;
                        }
                    }
                }
            }.start();
        }
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
                g.setColor(Color.black);
                g2d.setStroke(new BasicStroke(2f));
                g.drawRoundRect(x, y, width, height, arc, arc);
            }


            AffineTransform transform = new AffineTransform();
            transform.translate(x + 140, y + (image.getWidth() / 2));
            transform.rotate(rotation, (image.getWidth() / 2), (image.getHeight() / 2));

            g2d.drawImage(image, transform, observer);

            g.setColor(Color.white);
            g.setFont(CustomFont.light.deriveFont((float) fontSize));

            int stringWidth = g.getFontMetrics().stringWidth(text);
            int stringHeight = g.getFontMetrics().getHeight();

            g.drawString(text, x + (width - stringWidth) / 2, y + (height - stringHeight) / 2 + g.getFontMetrics().getAscent());

        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getX() > x && e.getX() < x + width && e.getY() > y + 30 && e.getY() < y + 30 + height) {
            switch (text) {
                case "SPIN": {
                    if (!homeScreen.isSpinOutlineAnimationRunning) {
                        isHovered = true;
                    }
                }
            }
        } else {
            switch (text) {
                case "SPIN": {
                    if (!homeScreen.isSpinOutlineAnimationRunning) {
                        isHovered = false;
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() > x && e.getX() < x + width && e.getY() > y + 30 && e.getY() < y + 30 + height) {
            switch (text) {
                case "SPIN": {
                    new Thread() { // Let button outline blink (Runs once per spin-start)
                        @Override
                        public void run() {
                            homeScreen.startSpin();

                            try {
                                isHovered = false;
                                Thread.sleep(250);
                                isHovered = true;

                                Thread.currentThread().interrupt();
                            } catch (Exception ex) {
                                Logger.error(ex.getMessage());
                            }
                        }
                    }.start();
                }
            }
        }
    }
}
