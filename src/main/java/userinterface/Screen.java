package userinterface;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Screen {

    public ArrayList<Component> components = new ArrayList<>();

    public Screen() {

    }

    public void draw(Graphics g, ImageObserver observer) {

    }

    public void mouseClicked(MouseEvent e) {
        for (Component component : components) {
            component.mouseClicked(e);
        }
    }

    public void mousePressed(MouseEvent e) {
        for (Component component : components) {
            component.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        for (Component component : components) {
            component.mouseReleased(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        for (Component component : components) {
            component.mouseDragged(e);
        }
    }

    public void mouseMoved(MouseEvent e) {
        for (Component component : components) {
            component.mouseMoved(e);
        }
    }

    public void keyTyped(KeyEvent e) {
        for (Component component : components) {
            component.keyTyped(e);
        }
    }

    public void keyPressed(KeyEvent e) {
        for (Component component : components) {
            component.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        for (Component component : components) {
            component.keyReleased(e);
        }
    }

}
