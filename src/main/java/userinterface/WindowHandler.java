package userinterface;

import userinterface.screens.HomeScreen;
import utils.CustomFont;
import utils.Logger;
import core.SlotMachine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class WindowHandler {

    public static JFrame window;

    public static Screen screen = new HomeScreen();

    public static void open() {
        Logger.trace("Opening window...");

        CustomFont.registerAll();

        JComponent component = new JComponent() {

            @Override
            public void paint(Graphics g) {
                g.setColor(Color.decode("#FFFFFF"));
                g.fillRect(0, 0, window.getWidth(), window.getHeight());

                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

                screen.draw(g, this);
            }

        };

        window = new JFrame();

        window.add(component);
        window.setSize(640, 860);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Logger.info("Exiting program...");
                System.exit(-1);
            }
        });

        window.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                screen.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                screen.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                screen.mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        window.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                screen.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                screen.mouseMoved(e);
            }
        });

        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                screen.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                screen.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                screen.keyReleased(e);
            }
        });

        try {
            window.setIconImage(
                    ImageIO.read(new File("assets/images/favicon.png")));
        } catch (Exception e) {
            Logger.warn("Unable to read favicon image (" + e.getMessage() + ")");
        }
        window.setTitle("Slot Machine | v" + SlotMachine.version);

        window.setVisible(true);

        if (!window.isVisible()) {
            Logger.error("Unable to open window");
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
                    Logger.error(e.getMessage());
                }
            }
        }.start();

    }

}
