package userinterface.screens;

import userinterface.Component;
import userinterface.Screen;
import userinterface.WindowHandler;
import userinterface.components.SpinWheelComponent;
import utils.CustomFont;
import utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;

public class HomeScreen extends Screen {

    public ArrayList<SpinWheelComponent> finishedWheels = new ArrayList<>();
    private boolean drawShowResultRect = true;
    private boolean hoverSpinButton = false;
    private boolean spinOutlineAnimation = false;
    private boolean resultRectAnimation = false;
    private boolean currentlySpinning = false;

    private int spinImageRotation = 0;

    public HomeScreen() {
        SpinWheelComponent firstSpinWheel = new SpinWheelComponent(1, 100, 215, 50, 50);
        SpinWheelComponent secondSpinWheel = new SpinWheelComponent(2, 255, 215, 50, 50);
        SpinWheelComponent thirdSpinWheel = new SpinWheelComponent(3, 410, 215, 50, 50);

        components.add(firstSpinWheel);
        components.add(secondSpinWheel);
        components.add(thirdSpinWheel);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (finishedWheels.contains(firstSpinWheel) && finishedWheels.contains(secondSpinWheel) && finishedWheels.contains(thirdSpinWheel)) {
                        if (!resultRectAnimation) {
                            animateResultRect();
                            currentlySpinning = false;
                            finishedWheels.clear();
                        }
                    } else {
                        System.out.print("");
                    }
                }
            }
        }.start();

    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {

        for (Component component : components) {
            component.draw(g, observer);
        }

        if (drawShowResultRect) {
            int rectWidth = (3 * 125) + (30 * 2) + 20;
            GradientPaint rectGradient = new GradientPaint(90, 360, Color.decode("#4834D4"), 90 + rectWidth, 505, Color.decode("#B500FF"));

            ((Graphics2D) g).setPaint(rectGradient);
            ((Graphics2D) g).setStroke(new BasicStroke(3f));
            g.drawRoundRect(90, 360, rectWidth, 145, 10, 10);
        }

        try {
            g.drawImage(ImageIO.read(new File("assets/images/logo.png")), 115, 60, 95, 95, observer);
        } catch (Exception e) {
            Logger.warn("Unable to read logo image (" + e.getMessage() + ")");
        }

        g.setColor(Color.black);
        g.setFont(CustomFont.medium.deriveFont(40f));
        g.drawString("SLOT MACHINE", 215, 120);

        GradientPaint spinGradient = new GradientPaint(230, 750, Color.decode("#4834D4"), 410, 750, Color.decode("#B500FF"));
        ((Graphics2D) g).setPaint(spinGradient);
        g.fillRoundRect(230, 700, 180, 50, 50, 50);

        if (hoverSpinButton) {
            g.setColor(Color.black);
            ((Graphics2D) g).setStroke(new BasicStroke(2f));
            g.drawRoundRect(230, 700, 180, 50, 50, 50);
        }

        try {
            BufferedImage image = resize(ImageIO.read(new File("assets/images/home/spin.png")), 25, 25);

            AffineTransform transform = new AffineTransform();
            transform.translate(370, 712);
            transform.rotate(Math.toRadians(spinImageRotation), 25 / 2, 25 / 2);

            ((Graphics2D) g).drawImage(image, transform, observer);
        } catch (Exception e) {
            Logger.warn("Unable to read spin image (" + e.getMessage() + ")");
        }

        g.setColor(Color.white);
        g.setFont(CustomFont.light.deriveFont(24f));
        g.drawString("SPIN", 295, 733);

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getX() > 235 && e.getX() < 425 && e.getY() > 725 && e.getY() < 775) {
            WindowHandler.window.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!spinOutlineAnimation) {
                hoverSpinButton = true;
            }
        } else {
            WindowHandler.window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if (!spinOutlineAnimation) {
                hoverSpinButton = false;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() > 235 && e.getX() < 425 && e.getY() > 725 && e.getY() < 775) {
            animateSpinImage();

            for (Component component : components) {
                if (component instanceof SpinWheelComponent) {
                    SpinWheelComponent wheel = ((SpinWheelComponent) component);
                    if (!wheel.rolling) {
                        Logger.trace("Start spin wheel #" + wheel.number);
                        wheel.startRolling();
                    }
                }
            }

            new Thread() {
                @Override
                public void run() {
                    try {
                        spinOutlineAnimation = true;
                        hoverSpinButton = false;
                        Thread.sleep(250);
                        hoverSpinButton = true;
                        spinOutlineAnimation = false;
                    } catch (Exception ex) {

                    }
                }
            }.start();

        }
    }

    private void animateSpinImage() {
        currentlySpinning = true;

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (currentlySpinning) {
                        try {
                            Thread.sleep(2);
                            spinImageRotation++;
                        } catch (Exception e) {

                        }
                    } else {
                        spinImageRotation = 0;
                        break;
                    }
                }
            }
        }.start();
    }

    private void animateResultRect() {
        resultRectAnimation = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    int count = 0;

                    while (count < 10) {
                        Thread.sleep(250);
                        drawShowResultRect = !drawShowResultRect;
                        count++;
                    }

                    resultRectAnimation = false;
                } catch (Exception e) {

                }
            }
        }.start();
    }

}
