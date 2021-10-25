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
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

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
    private GradientButtonComponent spinButton;

    public HomeScreen() {
        firstWheel = new SpinWheelComponent(this, 1, 100, 180, 50, 50, 0);
        secondWheel = new SpinWheelComponent(this, 2, 255, 180, 50, 50, 0);
        thirdWheel = new SpinWheelComponent(this, 3, 410, 180, 50, 50, 0);

        generateStopTimes();

        try {
            spinButton = new GradientButtonComponent(this, 230, 665, 180, 50, "SPIN", 24, ImageIO.read(new File("assets/images/home/spin.png")), Color.decode("#4834D4"), Color.decode("#B500FF"), 50);
            components.add(spinButton);
            balanceBadge = new GradientBadgeComponent(this, 80, 730, 135, 40, "YOUR BALANCE:", "10.00", 10, 16, ImageIO.read(new File("assets/images/home/dollar.png")), Color.decode("#EB4D4B"), Color.decode("#F0932B"), 40);
            components.add(balanceBadge);
            streakBadge = new GradientBadgeComponent(this, 430, 730, 135, 40, "WIN STREAK:", "0", 10, 16, ImageIO.read(new File("assets/images/home/streak.png")), Color.decode("#11B5C6"), Color.decode("#2ECC71"), 40);
            components.add(streakBadge);
        } catch (IOException e) {
            Logger.warn("Unable to read image (" + e.getMessage() + ")");
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
                            Logger.logic("Detected spin end!");
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

    public void generateStopTimes() {
        ArrayList<Integer> times = new ArrayList<>();
        times.add(new Random().nextInt(10000 - 5000) + 5000);
        times.add(new Random().nextInt(10000 - 5000) + 5000);
        times.add(new Random().nextInt(10000 - 5000) + 5000);

        Collections.sort(times);

        Logger.logic("Generated stop times for wheels! " + times);

        firstWheel.timeTillStopping = times.get(0);
        secondWheel.timeTillStopping = times.get(1);
        thirdWheel.timeTillStopping = times.get(2);
    }

    public void updateBalance(double multiplier) {
        new Thread() {
            @Override
            public void run() {
                try {
                    double current = Double.parseDouble(balanceBadge.value);
                    String newValue = String.valueOf(new DecimalFormat("##.00").format(current * multiplier)).replace(",", ".");

                    if (multiplier > 1) {
                        balanceBadge.setValue(newValue, true, ColorUtils.green);
                    } else {
                        balanceBadge.setValue(newValue, true, ColorUtils.red);
                    }

                    Thread.sleep(1000);

                    balanceBadge.setValue(newValue, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void increaseStreak() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String newValue = String.valueOf(Integer.parseInt(streakBadge.value) + 1);

                    streakBadge.setValue(newValue, true, ColorUtils.green);

                    Thread.sleep(1000);

                    streakBadge.setValue(newValue, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void resetStreak() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String newValue = "0";

                    streakBadge.setValue(newValue, true, ColorUtils.green);

                    Thread.sleep(1000);

                    streakBadge.setValue(newValue, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
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
        Logger.logic("Starting new spin...");

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
                                Logger.animation("Finished lower-opacity animation of 'SPIN' button!");
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            Logger.animation("Started lower-opacity animation of 'SPIN' button!");
        }

        for (Component component : components) {
            if (component instanceof SpinWheelComponent) {
                SpinWheelComponent wheel = ((SpinWheelComponent) component);
                if (!wheel.isRolling) {
                    wheel.roll();
                    Logger.logic("Started spin of wheel #" + wheel.index + "!");
                }
            }
        }


        new Thread() { // Variables for + pin buttons outline animation + interrupting result highlighter animation (Runs once per spin-start)
            @Override
            public void run() {
                try {
                    isSpinOutlineAnimationRunning = true;
                    Thread.sleep(250);
                    isSpinOutlineAnimationRunning = false;

                    if (isResultHighlighterAnimationRunning) {
                        interruptResultHighlighterAnimation = true;
                        Logger.animation("Interrupted breathing-opacity of result highlighter!");
                    }

                    Thread.currentThread().interrupt();
                } catch (Exception ex) {
                    Logger.error(ex.getMessage());
                }
            }
        }.start();

        Logger.logic("Started new spin!");
    }

    public void animateSpinImage() {
        currentlySpinning = true;
        spinButton.rotateImage();
    }

    private void processResult() {
        Logger.logic("Processing spin result...");

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
            Logger.logic("Spin Result: ONLY ONCE (-10%)");
            updateBalance(0.9);
            resetStreak();
        } else if (!thrice) {
            Logger.logic("Spin Result: ONE TWICE (+0%)");
        } else {
            Logger.logic("Spin Result: ONE THRICE (+10%)");
            updateBalance(1.1);
            increaseStreak();
        }

    }

    private void animateResultHighlighter() {
        Logger.animation("Started breathing-opacity animation of result highlighter!");

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
                                Logger.animation("Stopped breathing-opacity animation of result highlighter after it's progress came down to 0.5!");
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
