import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Brick extends Rectangle {
    public static final double WIDTH = 78;
    public static final double HEIGHT = 22;

    protected int hits;
    private int points;

    public Brick(double x, double y, int hits, int points) {
        super(x, y, WIDTH, HEIGHT);
        this.hits = hits;
        this.points = points;

        setArcWidth(6);
        setArcHeight(6);
        updateColor();
    }

    public boolean hit() {
        hits--;
        updateColor();

        if (hits <= 0) {
            return true;
        }
        return false;
    }

    public boolean isBreakable() {
        return true;
    }

    public int getPoints() {
        return points;
    }

    protected void updateColor() {
        if (hits == 1) {
            setFill(Color.LIMEGREEN);
        } else if (hits == 2) {
            setFill(Color.ORANGE);
        } else {
            setFill(Color.CRIMSON);
        }
    }
}