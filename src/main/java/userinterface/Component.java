package userinterface;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

public class Component {

    public int x;
    public int y;
    public int width;
    public int height;

    public Component(Screen parent, int relativeX, int relativeY, int width, int height) {
        this.x = relativeX;
        this.y = relativeY;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g, ImageObserver observer) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

}
