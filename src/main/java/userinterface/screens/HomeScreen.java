package userinterface.screens;

import userinterface.Component;
import userinterface.Screen;
import userinterface.components.SpinWheelComponent;
import utils.CustomFont;
import utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;

public class HomeScreen extends Screen {

    private boolean drawShowResultRect = true;

    public HomeScreen() {
        SpinWheelComponent firstSpinWheel = new SpinWheelComponent(1, 100, 215, 50, 50);
        SpinWheelComponent secondSpinWheel = new SpinWheelComponent(2, 255, 215, 50, 50);
        SpinWheelComponent thirdSpinWheel = new SpinWheelComponent(3, 410, 215, 50, 50);

        components.add(firstSpinWheel);
        components.add(secondSpinWheel);
        components.add(thirdSpinWheel);

        ArrayList<SpinWheelComponent> finished = new ArrayList<>();

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    for (Component component : components) {
                        if (component instanceof SpinWheelComponent) {
                            if (!((SpinWheelComponent) component).rolling) {
                                finished.add(((SpinWheelComponent) component));
                            }
                        }
                    }

                    if (finished.contains(firstSpinWheel) && finished.contains(secondSpinWheel) && finished.contains(thirdSpinWheel)) {
                        animateResultRect();
                        break;
                    }
                }
            }
        }.start();

    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {

        for (Component component : components) {
            component.draw(g, observer);
        }

        if (drawShowResultRect) {
            int rectWidth = (3 * 125) + (30 * 2) + 20;
            GradientPaint gradient = new GradientPaint(90, 360, Color.decode("#4834D4"), 90 + rectWidth, 505, Color.decode("#B500FF"));

            ((Graphics2D) g).setPaint(gradient);
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

    }

    private void animateResultRect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    int count = 0;

                    while(count < 10) {
                        Thread.sleep(250);
                        drawShowResultRect = !drawShowResultRect;
                        count ++;
                    }
                } catch (Exception e) {

                }
            }
        }.start();
    }

}
