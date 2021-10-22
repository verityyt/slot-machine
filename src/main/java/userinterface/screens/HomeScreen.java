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
    private boolean drawResultHighlighter = true;
    private boolean isSpinButtonHovered = false;
    private boolean isSpinOutlineAnimationRunning = false;
    private boolean isResultHighlighterAnimationRunning = false;
    private boolean interruptResultHighlighterAnimation = false;
    private boolean currentlySpinning = false;

    private int spinImageRotation = 0;

    public HomeScreen() {
        SpinWheelComponent firstWheel = new SpinWheelComponent(1, 100, 180, 50, 50);
        SpinWheelComponent secondWheel = new SpinWheelComponent(2, 255, 180, 50, 50);
        SpinWheelComponent thirdWheel = new SpinWheelComponent(3, 410, 180, 50, 50);

        components.add(firstWheel);
        components.add(secondWheel);
        components.add(thirdWheel);

        new Thread() { // Whether all wheels have finished => Start result highlighter animation (Runs infinitely)
            @Override
            public void run() {
                while (true) {
                    if (finishedWheels.contains(firstWheel) && finishedWheels.contains(secondWheel) && finishedWheels.contains(thirdWheel)) {
                        if (!isResultHighlighterAnimationRunning) {
                            animateResultHighlighter();
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

    public static BufferedImage resize(BufferedImage img, int width, int height) {
        Image temp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();

        return image;
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {
        Graphics2D g2d = (Graphics2D) g;

        for (Component component : components) {
            component.draw(g, observer);
        }

        /* RESULT RECT */

        if (drawResultHighlighter) {
            int rectWidth = (3 * 125) + (30 * 2) + 20;
            if(currentlySpinning) {
                AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                g2d.setComposite(composite);
            }else {
                AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
                g2d.setComposite(composite);
            }
            g2d.setPaint(new GradientPaint(90, 325, Color.decode("#4834D4"), 90 + rectWidth, 470, Color.decode("#B500FF")));
            g2d.setStroke(new BasicStroke(3f));
            g.drawRoundRect(90, 325, rectWidth, 145, 10, 10);

            AlphaComposite defaultComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
            g2d.setComposite(defaultComposite);
        }

        /* HEADER */

        try {
            g.drawImage(ImageIO.read(new File("assets/images/logo.png")), 105, 40, 95, 95, observer);
        } catch (Exception e) {
            Logger.warn("Unable to read logo image (" + e.getMessage() + ")");
        }

        g.setColor(Color.black);
        g.setFont(CustomFont.medium.deriveFont(40f));
        g.drawString("SLOT MACHINE", 205, 100);

        /* SPIN BUTTON */

        g2d.setPaint(new GradientPaint(230, 715, Color.decode("#4834D4"), 410, 715, Color.decode("#B500FF")));
        g.fillRoundRect(230, 665, 180, 50, 50, 50);

        if (isSpinButtonHovered) {
            g.setColor(Color.black);
            g2d.setStroke(new BasicStroke(2f));
            g.drawRoundRect(230, 665, 180, 50, 50, 50);
        }

        try {
            BufferedImage image = resize(ImageIO.read(new File("assets/images/home/spin.png")), 25, 25);

            AffineTransform transform = new AffineTransform();
            transform.translate(370, 677);
            transform.rotate(Math.toRadians(spinImageRotation), 12, 12);

            g2d.drawImage(image, transform, observer);
        } catch (Exception e) {
            Logger.warn("Unable to read spin image (" + e.getMessage() + ")");
        }

        g.setColor(Color.white);
        g.setFont(CustomFont.light.deriveFont(24f));
        g.drawString("SPIN", 295, 698);

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getX() > 235 && e.getX() < 425 && e.getY() > 690 && e.getY() < 740) {
            WindowHandler.window.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!isSpinOutlineAnimationRunning) {
                isSpinButtonHovered = true;
            }
        } else {
            WindowHandler.window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if (!isSpinOutlineAnimationRunning) {
                isSpinButtonHovered = false;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() > 235 && e.getX() < 425 && e.getY() > 690 && e.getY() < 740) {
            if (!currentlySpinning) {
                animateSpinImage();
            }

            for (Component component : components) {
                if (component instanceof SpinWheelComponent) {
                    SpinWheelComponent wheel = ((SpinWheelComponent) component);
                    if (!wheel.isRolling) {
                        Logger.trace("Start spin wheel #" + wheel.index);
                        wheel.roll();
                    }
                }
            }

            new Thread() { // Spin buttons outline animation + interrupting result highlighter animation (Runs once per spin-start)
                @Override
                public void run() {
                    try {
                        isSpinOutlineAnimationRunning = true;
                        isSpinButtonHovered = false;
                        Thread.sleep(250);
                        isSpinButtonHovered = true;
                        isSpinOutlineAnimationRunning = false;

                        interruptResultHighlighterAnimation = true;
                        Thread.sleep(500);
                        interruptResultHighlighterAnimation = false;

                        Thread.currentThread().interrupt();
                    } catch (Exception ex) {
                        Logger.error(ex.getMessage());
                    }
                }
            }.start();

        }
    }

    private void animateSpinImage() {
        currentlySpinning = true;

        new Thread() { // Spin image rotation animation (Runs once per spin-start)
            @Override
            public void run() {
                while (true) {
                    if (currentlySpinning) {
                        try {
                            Thread.sleep(2);
                            spinImageRotation++;
                        } catch (Exception e) {
                            Logger.error(e.getMessage());
                        }
                    } else {
                        spinImageRotation = 0;
                        break;
                    }
                }
            }
        }.start();
    }

    private void animateResultHighlighter() {
        isResultHighlighterAnimationRunning = true;

        new Thread() { // Result highlighter animation (Runs once per spin-end, maybe interrupted by new spin-start)
            @Override
            public void run() {
                try {
                    int count = 0;

                    while (count < 10) {
                        if (interruptResultHighlighterAnimation) {
                            drawResultHighlighter = true;
                            break;
                        }

                        Thread.sleep(250);
                        drawResultHighlighter = !drawResultHighlighter;
                        count++;
                    }

                    isResultHighlighterAnimationRunning = false;
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    Logger.error(e.getMessage());
                }
            }
        }.start();
    }

}
