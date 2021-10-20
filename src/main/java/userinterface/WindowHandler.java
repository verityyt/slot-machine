package userinterface;

import utils.Logger;
import core.SlotMachine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class WindowHandler {

    public static JFrame window;

    public static void open() {
        Logger.trace("Opening window...");

        JComponent component = new JComponent() {

            @Override
            public void paint(Graphics g) {

            }

        };

        window = new JFrame();

        window.add(component);
        window.setSize(640, 860);
        window.setResizable(false);
        try {
            Logger.trace("Reading icon image...");

            window.setIconImage(
                    ImageIO.read(new File("assets/images/favicon.png")));
        } catch (Exception e) {
            Logger.error("Unable to read icon image (" + e.getMessage() + ")");
        }
        window.setTitle("Slot Machine | v" + SlotMachine.version);

        window.setVisible(true);

        if(window.isVisible()) {
            Logger.info("Opened window");
        }else {
            Logger.error("Unable to open window");
            System.exit(-1);
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000 / 60);
                        window.repaint();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

}
