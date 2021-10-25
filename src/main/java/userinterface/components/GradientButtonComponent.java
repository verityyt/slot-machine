package userinterface.components;

import userinterface.Component;
import userinterface.Screen;
import userinterface.WindowHandler;
import userinterface.screens.HomeScreen;
import userinterface.screens.StartScreen;
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
    private final Screen parent;
    public boolean isRotating = false;
    public double rotation = 0.0;
    private HomeScreen homeScreen = null;
    private boolean isHovered = false;


    public GradientButtonComponent(Screen parent, int x, int y, int width, int height, String text, int fontSize, BufferedImage image, Color gradientStart, Color gradientEnd, int arc) {
        super(parent, x, y, width, height);
        if (image != null) {
            this.image = ImageUtils.resize(image, 25, 25);
        } else {
            this.image = null;
        }
        this.fontSize = fontSize;
        GradientButtonComponent.text = text;
        this.gradientStart = gradientStart;
        this.gradientEnd = gradientEnd;
        this.arc = arc;
        this.parent = parent;
    }

    public void setText(String newText) {
        text = newText;
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
                            e.printStackTrace();
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

        if (WindowHandler.screen instanceof HomeScreen && homeScreen == null) {
            homeScreen = (HomeScreen) WindowHandler.screen;
        }

        g2d.setPaint(new GradientPaint(parent.x + x, parent.y + y + (height / 2), gradientStart, x + width, y + (height / 2), gradientEnd));
        g.fillRoundRect(parent.x + x, parent.y + y, width, height, arc, arc);

        if (isHovered) {
            g.setColor(Color.black);
            g2d.setStroke(new BasicStroke(2f));
            g.drawRoundRect(parent.x + x, parent.y + y, width, height, arc, arc);
        }

        if (image != null) {
            AffineTransform transform = new AffineTransform();
            transform.translate(parent.x + x + 140, parent.y + y + (image.getWidth() / 2));
            transform.rotate(rotation, (image.getWidth() / 2), (image.getHeight() / 2));

            g2d.drawImage(image, transform, observer);
        }

        g.setColor(Color.white);
        g.setFont(CustomFont.light.deriveFont((float) fontSize));

        int stringWidth = g.getFontMetrics().stringWidth(text);
        int stringHeight = g.getFontMetrics().getHeight();

        g.drawString(text, parent.x + x + (width - stringWidth) / 2, parent.y + y + (height - stringHeight) / 2 + g.getFontMetrics().getAscent());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getX() > parent.x + x && e.getX() < parent.x + x + width && e.getY() > parent.y + y + 30 && e.getY() < parent.y + y + 30 + height) {
            switch (text) {
                case "SPIN": {
                    if (homeScreen != null && !homeScreen.isSpinOutlineAnimationRunning) {
                        isHovered = true;
                    }
                }
                case "PLAY": {
                    isHovered = true;
                }
            }
        } else {
            switch (text) {
                case "SPIN": {
                    if (homeScreen != null && !homeScreen.isSpinOutlineAnimationRunning) {
                        isHovered = false;
                    }
                }
                case "PLAY": {
                    isHovered = false;
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() > parent.x + x && e.getX() < parent.x + x + width && e.getY() > parent.y + y + 30 && e.getY() < parent.y + y + 30 + height) {
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
                case "PLAY": {
                    if (WindowHandler.screen instanceof StartScreen) {
                        WindowHandler.switchScreen(new HomeScreen());
                    }
                }
            }
        }
    }
}
