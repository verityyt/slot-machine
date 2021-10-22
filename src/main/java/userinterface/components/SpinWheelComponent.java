package userinterface.components;

import userinterface.Component;
import userinterface.WindowHandler;
import userinterface.screens.HomeScreen;
import utils.Logger;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Random;

public class SpinWheelComponent extends Component {

    public final int number;
    private final Color red = Color.decode("#E74C3C");
    private final Color blue = Color.decode("#3498DB");
    private final Color green = Color.decode("#2ECC71");
    public boolean rolling = false;
    private Color firstColor = red;
    private Color secondColor = green;
    private Color thirdColor = blue;
    private Color fourthColor = blue;
    private int firstY;
    private int secondY;
    private int thirdY;
    private int fourthY;
    private int rollingSpeed = 1;

    private SpinWheelComponent component = this;

    public SpinWheelComponent(int number, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.width = 125;
        this.height = 435;
        this.firstY = y;
        this.secondY = y + 155;
        this.thirdY = y + 310;
        this.fourthY = y;
        this.number = number;
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {

        GradientPaint fourthGradient = new GradientPaint(x + 62, y, Color.white, x + 62, y + width, fourthColor);

        ((Graphics2D) g).setPaint(fourthGradient);
        g.fillOval(x, fourthY - width - 30, width, width);

        GradientPaint thirdGradient = new GradientPaint(x + 62, y + 310, thirdColor, x + 62, y + 310 + width, Color.white);

        ((Graphics2D) g).setPaint(thirdGradient);
        g.fillOval(x, thirdY, width, width);

        GradientPaint secondGradient = new GradientPaint(x + 62, y + 155 + width, secondColor, x + 62, y + 155 + width + width, Color.white);

        ((Graphics2D) g).setPaint(secondGradient);
        g.fillOval(x, secondY, width, width);

        GradientPaint firstGradient = new GradientPaint(x + 62, y, Color.white, x + 62, y + width, firstColor);

        ((Graphics2D) g).setPaint(firstGradient);
        g.fillOval(x, firstY, width, width);

    }

    public void startRolling() {
        rolling = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (!rolling) {
                            break;
                        }

                        Thread.sleep(rollingSpeed);

                        firstY++;
                        secondY++;
                        thirdY++;

                        fourthY++;

                        if (firstY == (y + 155)) {

                            int tempFirstY = firstY;
                            int tempSecondY = secondY;
                            int tempFourthY = fourthY;

                            Color tempFirstColor = firstColor;
                            Color tempSecondColor = secondColor;
                            Color tempFourthColor = fourthColor;

                            fourthY = y;
                            firstY = tempFourthY - width - 30;
                            secondY = tempFirstY;
                            thirdY = tempSecondY;

                            firstColor = tempFourthColor;
                            secondColor = tempFirstColor;
                            thirdColor = tempSecondColor;
                            fourthColor = tempSecondColor;

                        }
                    }

                } catch (Exception e) {

                }

            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    int sleep = new Random().nextInt(10000 - 5000) + 5000;

                    Thread.sleep(sleep);
                    Logger.debug("Stopping spin wheel #" + number + " after " + sleep + "ms");

                    while (true) {
                        if (rollingSpeed < 5) {
                            rollingSpeed++;

                            Thread.sleep(750);
                        } else {
                            if (firstY == y || secondY == y || thirdY == y || fourthY == y) {
                                rolling = false;
                                rollingSpeed = 1; // Resetting rolling speed
                                if (WindowHandler.screen instanceof HomeScreen) {
                                    ((HomeScreen) WindowHandler.screen).finishedWheels.add(component);
                                }
                                break;
                            } else {
                                System.out.print(""); // Do nothing
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }.start();
    }

}
