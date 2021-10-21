package userinterface.screens;

import userinterface.Screen;
import utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;

public class HomeScreen extends Screen {

    @Override
    public void draw(Graphics g, ImageObserver observer) {

        try {
            g.drawImage(ImageIO.read(new File("assets/images/logo.png")), 133, 61,94,94, observer);
        } catch (Exception e) {
            Logger.warn("Unable to read logo image (" + e.getMessage() + ")");
        }


    }

}
