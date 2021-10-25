package userinterface.components;

import userinterface.Component;
import userinterface.Screen;
import userinterface.WindowHandler;
import userinterface.screens.HomeScreen;
import utils.ColorUtils;
import utils.Logger;

import java.awt.*;
import java.awt.image.ImageObserver;

public class SpinWheelComponent extends Component {

    public final int index;
    private final SpinWheelComponent component = this;
    private final Screen parent;
    public int timeTillStopping;
    public boolean isRolling = false;
    private Color firstCircleColor = ColorUtils.red;
    private Color secondCircleColor = ColorUtils.green;
    private Color thirdCircleColor = ColorUtils.blue;
    private Color fourthCircleColor = ColorUtils.blue;
    private int rollingSpeed = 1;
    private int firstCircleY;
    private int secondCircleY;
    private int thirdCircleY;
    private int fourthCircleY;

    public SpinWheelComponent(Screen parent, int number, int x, int y, int width, int height, int timeTillStopping) {
        super(parent, x, y, width, height);
        this.index = number;
        this.firstCircleY = y;
        this.secondCircleY = y + 155;
        this.thirdCircleY = y + 310;
        this.fourthCircleY = y;
        this.width = 125;
        this.height = 435;
        this.parent = parent;
        this.timeTillStopping = timeTillStopping;
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {

        ((Graphics2D) g).setPaint(new GradientPaint(parent.x + x + 62, parent.y + y, Color.white, parent.x + x + 62, parent.y + y + width, fourthCircleColor));
        g.fillOval(parent.x + x, parent.y + fourthCircleY - width - 30, width, width);

        ((Graphics2D) g).setPaint(new GradientPaint(parent.x + x + 62, parent.y + y + 310, thirdCircleColor, parent.x + x + 62, parent.y + y + 310 + width, Color.white));
        g.fillOval(parent.x + x, parent.y + thirdCircleY, width, width);

        ((Graphics2D) g).setPaint(new GradientPaint(parent.x + x + 62, parent.y + y + 155 + width, secondCircleColor, parent.x + x + 62, parent.y + y + 155 + width + width, Color.white));
        g.fillOval(parent.x + x, parent.y + secondCircleY, width, width);

        ((Graphics2D) g).setPaint(new GradientPaint(parent.x + x + 62, parent.y + y, Color.white, parent.x + x + 62, parent.y + y + width, firstCircleColor));
        g.fillOval(parent.x + x, parent.y + firstCircleY, width, width);

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

                        if (firstCircleY == (parent.y + y + 155)) {

                            int tempFirstCircleY = firstCircleY;
                            int tempSecondCircleY = secondCircleY;
                            int tempFourthCircleY = fourthCircleY;

                            Color tempFirstCircleColor = firstCircleColor;
                            Color tempSecondCircleColor = secondCircleColor;
                            Color tempFourthCircleColor = fourthCircleColor;

                            fourthCircleY = parent.y + y;
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
                    e.printStackTrace();
                }

            }
        }.start();

        Logger.animation("Started spin animation of wheel #" + index + "!");

        new Thread() { // Stopping + animation (runs once per spin-start)
            @Override
            public void run() {
                try {
                    Thread.sleep(timeTillStopping);

                    while (true) {
                        if (rollingSpeed < 5) {
                            rollingSpeed++;

                            Thread.sleep(750);
                        } else {
                            if (firstCircleY == parent.y + y || secondCircleY == parent.y + y || thirdCircleY == parent.y + y || fourthCircleY == parent.y + y) {
                                isRolling = false;
                                rollingSpeed = 1;
                                if (WindowHandler.screen instanceof HomeScreen) {
                                    ((HomeScreen) WindowHandler.screen).finishedWheels.add(component);
                                }
                                Logger.animation("Finished spin animation of wheel #" + index + " after " + timeTillStopping + "ms!");
                                break;
                            } else {
                                System.out.print("");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
