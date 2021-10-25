package userinterface;

public class Easings {

    public static int easeInOutCubic(double x) {
        return (int) Math.round(x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2);
    }

}
