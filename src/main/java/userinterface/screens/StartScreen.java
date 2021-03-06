package userinterface.screens;

import userinterface.Component;
import userinterface.Screen;
import userinterface.components.GradientButtonComponent;
import utils.CustomFont;
import utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;

public class StartScreen extends Screen {

    public int animationContentTopY = 0;
    GradientButtonComponent button;

    public StartScreen() {
        button = new GradientButtonComponent(this, 230, 500, 180, 50, "PLAY", 24, null, Color.decode("#4834D4"), Color.decode("#B500FF"), 50);
        components.add(button);
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {

        for (Component component : components) {
            component.draw(g, observer);
        }

        try {
            g.drawImage(ImageIO.read(new File("assets/images/logo.png")), x + 235, animationContentTopY + 200, 150, 150, observer);
        } catch (Exception e) {
            Logger.warn("Unable to read logo image (" + e.getMessage() + ")");
        }

        g.setColor(Color.black);
        g.setFont(CustomFont.medium.deriveFont(48f));
        g.drawString("SLOT MACHINE", x + 140, animationContentTopY + 390);

    }

}
