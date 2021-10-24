package userinterface.screens;

import userinterface.Component;
import userinterface.Screen;
import userinterface.WindowHandler;
import userinterface.components.GradientBadgeComponent;
import userinterface.components.GradientButtonComponent;
import userinterface.components.SpinWheelComponent;
import utils.ColorUtils;
import utils.CustomFont;
import utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeScreen extends Screen {

    private final SpinWheelComponent firstWheel;
    private final SpinWheelComponent secondWheel;
    private final SpinWheelComponent thirdWheel;
    public ArrayList<SpinWheelComponent> finishedWheels = new ArrayList<>();
    public boolean isSpinOutlineAnimationRunning = false;
    public boolean interruptResultHighlighterAnimation = false;
    public boolean currentlySpinning = false;
    private double resultHighlighterOpacity = 1.0;
    private boolean isResultHighlighterAnimationRunning = false;
    private GradientBadgeComponent balanceBadge;
    private GradientBadgeComponent streakBadge;

    public HomeScreen() {
        firstWheel = new SpinWheelComponent(this, 1, 100, 180, 50, 50);
        secondWheel = new SpinWheelComponent(this, 2, 255, 180, 50, 50);
        thirdWheel = new SpinWheelComponent(this, 3, 410, 180, 50, 50);

        try {
            GradientButtonComponent spinButton = new GradientButtonComponent(this, 230, 665, 180, 50, "SPIN", 24, ImageIO.read(new File("assets/images/home/spin.png")), Color.decode("#4834D4"), Color.decode("#B500FF"), 50);
            components.add(spinButton);
            balanceBadge = new GradientBadgeComponent(this, 80, 730, 135, 40, "YOUR BALANCE:", "10.00", 10, 16, ImageIO.read(new File("assets/images/home/dollar.png")), Color.decode("#EB4D4B"), Color.decode("#F0932B"), 40);
            components.add(balanceBadge);
            streakBadge = new GradientBadgeComponent(this, 430, 730, 135, 40, "WIN STREAK:", "0", 10, 16, ImageIO.read(new File("assets/images/home/streak.png")), Color.decode("#11B5C6"), Color.decode("#2ECC71"), 40);
            components.add(streakBadge);
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

    public void updateBalance(double multiplier) {
        double current = Double.parseDouble(balanceBadge.value);
        balanceBadge.value = String.valueOf(new DecimalFormat("##.00").format(current * multiplier)).replace(",", ".");
    }

    public void increaseStreak() {
        streakBadge.value = String.valueOf(Integer.parseInt(streakBadge.value) + 1);
    }

    public void resetStreak() {
        streakBadge.value = "0";
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

        int rectWidth = (3 * 125) + (30 * 2) + 20;

        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) resultHighlighterOpacity);
        g2d.setComposite(composite);

        g2d.setPaint(new GradientPaint(x + 90, y + 325, Color.decode("#4834D4"), x + 90 + rectWidth, y + 470, Color.decode("#B500FF")));
        g2d.setStroke(new BasicStroke(3f));
        g.drawRoundRect(x + 90, y + 325, rectWidth, 145, 10, 10);

        AlphaComposite defaultComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g2d.setComposite(defaultComposite);

        /* HEADER */

        try {
            g.drawImage(ImageIO.read(new File("assets/images/logo.png")), x + 105, y + 40, 95, 95, observer);
        } catch (Exception e) {
            Logger.warn("Unable to read logo image (" + e.getMessage() + ")");
        }

        g.setColor(Color.black);
        g.setFont(CustomFont.medium.deriveFont(40f));
        g.drawString("SLOT MACHINE", x + 205, y + 100);

        /* FOOTER */

        g.setColor(Color.decode("#999999"));
        g.setFont(CustomFont.regular.deriveFont(12f));

        String text = "ENTER OR SPACE TO START SPIN";
        int stringWidth = g.getFontMetrics().stringWidth(text);

        g.drawString(text, x + (640 / 2) - (stringWidth / 2), y + 810);


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

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 32 || e.getKeyCode() == 10) {
            startSpin();
        }
    }

    public void startSpin() {
        Logger.info("Starting new spin...");

        if (!currentlySpinning) {
            animateSpinImage();

            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(25);

                            if (resultHighlighterOpacity > 0.5) {
                                resultHighlighterOpacity -= 0.1;
                            } else {
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

        }

        for (Component component : components) {
            if (component instanceof SpinWheelComponent) {
                SpinWheelComponent wheel = ((SpinWheelComponent) component);
                if (!wheel.isRolling) {
                    Logger.trace("Starting spin wheel #" + wheel.index + "...");
                    wheel.roll();
                }
            }
        }

        new Thread() { // Spin buttons outline animation + interrupting result highlighter animation (Runs once per spin-start)
            @Override
            public void run() {
                try {
                    isSpinOutlineAnimationRunning = true;
                    Thread.sleep(250);
                    isSpinOutlineAnimationRunning = false;

                    if (isResultHighlighterAnimationRunning) {
                        interruptResultHighlighterAnimation = true;
                    }

                    Thread.currentThread().interrupt();
                } catch (Exception ex) {
                    Logger.error(ex.getMessage());
                }
            }
        }.start();
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

    private void processResult() {
        Container content = WindowHandler.window.getContentPane();
        BufferedImage image = new BufferedImage(640, 860, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        content.print(g);

        String firstWheelHex = ColorUtils.getHexFromRGB(image.getRGB((firstWheel.x + (firstWheel.width) / 2), (firstWheel.y + (firstWheel.height / 2))));
        String secondWheelHex = ColorUtils.getHexFromRGB(image.getRGB((secondWheel.x + (secondWheel.width) / 2), (secondWheel.y + (secondWheel.height / 2))));
        String thirdWheelHex = ColorUtils.getHexFromRGB(image.getRGB((thirdWheel.x + (thirdWheel.width) / 2), (thirdWheel.y + (thirdWheel.height / 2))));

        HashMap<String, Integer> frequency = new HashMap<>();

        frequency.put(firstWheelHex, 1);

        if (frequency.containsKey(secondWheelHex)) {
            frequency.put(secondWheelHex, frequency.get(secondWheelHex) + 1);
        } else {
            frequency.put(secondWheelHex, 1);
        }

        if (frequency.containsKey(thirdWheelHex)) {
            frequency.put(thirdWheelHex, frequency.get(thirdWheelHex) + 1);
        } else {
            frequency.put(thirdWheelHex, 1);
        }

        boolean twice = false;
        boolean thrice = false;

        for (String key : frequency.keySet()) {
            int value = frequency.get(key);

            switch (value) {
                case 1: {
                    break;
                }
                case 2: {
                    twice = true;
                    break;
                }
                case 3: {
                    thrice = true;
                    break;
                }
            }

        }

        if (!twice && !thrice) {
            Logger.info("Spin Result: ONLY ONCE (-10%)");
            updateBalance(0.9);
            resetStreak();
        } else if (!thrice) {
            Logger.info("Spin Result: ONE TWICE (+0%)");
        } else {
            Logger.info("Spin Result: ONE THRICE (+10%)");
            updateBalance(1.1);
            increaseStreak();
        }

    }

    private void animateResultHighlighter() {
        isResultHighlighterAnimationRunning = true;

        new Thread() { // Result highlighter animation (Runs once per spin-end, maybe interrupted by new spin-start)
            @Override
            public void run() {
                try {
                    processResult();
                    int count = 0;
                    boolean forward = true;

                    while (true) {
                        if (count < 5) {
                            Thread.sleep(50);

                            if (interruptResultHighlighterAnimation && String.valueOf(resultHighlighterOpacity).startsWith("0.5")) {
                                isResultHighlighterAnimationRunning = false;
                                interruptResultHighlighterAnimation = false;
                                Thread.currentThread().interrupt();
                                break;
                            }

                            if (forward) {
                                if (resultHighlighterOpacity < 0.9) {
                                    resultHighlighterOpacity += 0.1;
                                } else {
                                    count++;
                                    forward = false;
                                    resultHighlighterOpacity = 0.9;
                                }
                            } else {
                                if (resultHighlighterOpacity > 0.5) {
                                    resultHighlighterOpacity -= 0.1;
                                } else {
                                    forward = true;
                                    resultHighlighterOpacity = 0.6;
                                }
                            }
                        } else {
                            isResultHighlighterAnimationRunning = false;
                            Thread.currentThread().interrupt();
                            break;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
