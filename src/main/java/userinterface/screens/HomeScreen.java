package userinterface.screens;

import userinterface.Component;
import userinterface.Screen;
import userinterface.components.GradientButtonComponent;
import userinterface.components.SpinWheelComponent;
import utils.CustomFont;
import utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomeScreen extends Screen {

    public ArrayList<SpinWheelComponent> finishedWheels = new ArrayList<>();
    public boolean isSpinOutlineAnimationRunning = false;
    public boolean interruptResultHighlighterAnimation = false;
    public boolean currentlySpinning = false;
    private boolean drawResultHighlighter = true;
    private boolean isResultHighlighterAnimationRunning = false;

    public HomeScreen() {
        SpinWheelComponent firstWheel = new SpinWheelComponent(1, 100, 180, 50, 50);
        SpinWheelComponent secondWheel = new SpinWheelComponent(2, 255, 180, 50, 50);
        SpinWheelComponent thirdWheel = new SpinWheelComponent(3, 410, 180, 50, 50);
        try {
            GradientButtonComponent spinButton = new GradientButtonComponent(230, 665, 180, 50, "SPIN", 24, ImageIO.read(new File("assets/images/home/spin.png")), Color.decode("#4834D4"), Color.decode("#B500FF"), 50);
            components.add(spinButton);
        } catch (IOException e) {
            Logger.warn("Unable to read spin image (" + e.getMessage() + ")");
        }
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

    @Override
    public void draw(Graphics g, ImageObserver observer) {
        Graphics2D g2d = (Graphics2D) g;

        for (Component component : components) {
            if (component instanceof SpinWheelComponent) {
                component.draw(g, observer);
            }
        }

        /* RESULT RECT */

        if (drawResultHighlighter) {
            int rectWidth = (3 * 125) + (30 * 2) + 20;
            if (currentlySpinning) {
                AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                g2d.setComposite(composite);
            } else {
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

        /* COMPONENTS */

        for (Component component : components) {
            if (!(component instanceof SpinWheelComponent)) {
                component.draw(g, observer);
            }
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Component component : components) {
            component.mouseMoved(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (Component component : components) {
            component.mouseClicked(e);
        }
    }

    public void animateSpinImage() {
        currentlySpinning = true;

        for (Component component : components) {
            if (component instanceof GradientButtonComponent) {
                GradientButtonComponent gradientButtonComponent = (GradientButtonComponent) component;
                if (GradientButtonComponent.text.equals("SPIN")) {
                    gradientButtonComponent.rotateImage();
                }
            }
        }
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
