package userinterface.components;

import sun.rmi.runtime.Log;
import userinterface.Component;
import userinterface.WindowHandler;
import userinterface.screens.HomeScreen;
import utils.Logger;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Random;

public class SpinWheelComponent extends Component {

    public final int index;

    private final Color red = Color.decode("#E74C3C");
    private final Color blue = Color.decode("#3498DB");
    private final Color green = Color.decode("#2ECC71");
    private final SpinWheelComponent component = this;
    public boolean isRolling = false;
    private Color firstCircleColor = red;
    private Color secondCircleColor = green;
    private Color thirdCircleColor = blue;
    private Color fourthCircleColor = blue;
    private int rollingSpeed = 1;
    private int firstCircleY;
    private int secondCircleY;
    private int thirdCircleY;
    private int fourthCircleY;

    public SpinWheelComponent(int number, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.index = number;
        this.firstCircleY = y;
        this.secondCircleY = y + 155;
        this.thirdCircleY = y + 310;
        this.fourthCircleY = y;
        this.width = 125;
        this.height = 435;
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {

        ((Graphics2D) g).setPaint(new GradientPaint(x + 62, y, Color.white, x + 62, y + width, fourthCircleColor));
        g.fillOval(x, fourthCircleY - width - 30, width, width);

        ((Graphics2D) g).setPaint(new GradientPaint(x + 62, y + 310, thirdCircleColor, x + 62, y + 310 + width, Color.white));
        g.fillOval(x, thirdCircleY, width, width);

        ((Graphics2D) g).setPaint(new GradientPaint(x + 62, y + 155 + width, secondCircleColor, x + 62, y + 155 + width + width, Color.white));
        g.fillOval(x, secondCircleY, width, width);

        ((Graphics2D) g).setPaint(new GradientPaint(x + 62, y, Color.white, x + 62, y + width, firstCircleColor));
        g.fillOval(x, firstCircleY, width, width);

    }

    public void roll() {
        isRolling = true;

        new Thread() { // Rolling animation (runs once per spin-start)
            @Override
            public void run() {
                try {
                    while (true) {
                        if (!isRolling) {
                            break;
                        }

                        Thread.sleep(rollingSpeed);

                        firstCircleY++;
                        secondCircleY++;
                        thirdCircleY++;
                        fourthCircleY++;

                        if (firstCircleY == (y + 155)) {

                            int tempFirstCircleY = firstCircleY;
                            int tempSecondCircleY = secondCircleY;
                            int tempFourthCircleY = fourthCircleY;

                            Color tempFirstCircleColor = firstCircleColor;
                            Color tempSecondCircleColor = secondCircleColor;
                            Color tempFourthCircleColor = fourthCircleColor;

                            fourthCircleY = y;
                            firstCircleY = tempFourthCircleY - width - 30;
                            secondCircleY = tempFirstCircleY;
                            thirdCircleY = tempSecondCircleY;

                            firstCircleColor = tempFourthCircleColor;
                            secondCircleColor = tempFirstCircleColor;
                            thirdCircleColor = tempSecondCircleColor;
                            fourthCircleColor = tempSecondCircleColor;

                        }
                    }

                } catch (Exception e) {
                    Logger.error(e.getMessage());
                }

            }
        }.start();

        new Thread() { // Stopping + animation (runs once per spin-start)
            @Override
            public void run() {
                try {
                    int timeTillStopping = new Random().nextInt(10000 - 5000) + 5000;

                    Thread.sleep(timeTillStopping);
                    Logger.debug("Stopping spin wheel #" + index + " after " + timeTillStopping + "ms");

                    while (true) {
                        if (rollingSpeed < 5) {
                            rollingSpeed++;

                            Thread.sleep(750);
                        } else {
                            if (firstCircleY == y || secondCircleY == y || thirdCircleY == y || fourthCircleY == y) {
                                isRolling = false;
                                rollingSpeed = 1;
                                if (WindowHandler.screen instanceof HomeScreen) {
                                    ((HomeScreen) WindowHandler.screen).finishedWheels.add(component);
                                }
                                break;
                            } else {
                                System.out.print("");
                            }
                        }
                    }
                } catch (Exception e) {
                    Logger.error(e.getMessage());
                }
            }
        }.start();
    }

}
