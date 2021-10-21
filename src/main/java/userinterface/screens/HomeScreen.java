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

public class HomeScreen extends Screen {

    public HomeScreen() {
        SpinWheelComponent spinWheel = new SpinWheelComponent(100, 215, 50, 50);

        components.add(spinWheel);
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {

        for (Component component : components) {
            component.draw(g, observer);
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

}
