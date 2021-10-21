package userinterface.components;

import userinterface.Component;

import java.awt.*;
import java.awt.image.ImageObserver;

public class SpinWheelComponent extends Component {

    private final Color red = Color.decode("#E74C3C");
    private final Color blue = Color.decode("#3498DB");
    private final Color green = Color.decode("#2ECC71");

    private final Color first = red;
    private final Color second = green;
    private final Color third = blue;

    public SpinWheelComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.width = 125;
        this.height = 435;
    }

    @Override
    public void draw(Graphics g, ImageObserver observer) {

        GradientPaint firstGradient = new GradientPaint(x + 62, y, Color.white, x + 62, y + width, first);

        ((Graphics2D) g).setPaint(firstGradient);
        g.fillOval(x, y, width, width);

        g.setColor(second);
        g.fillOval(x, y + 155, width, width);

        GradientPaint thirdGradient = new GradientPaint(x + 62, y + 310, third, x + 62, y + 310 + width, Color.white);

        ((Graphics2D) g).setPaint(thirdGradient);
        g.fillOval(x, y + 310, width, width);

    }

}
