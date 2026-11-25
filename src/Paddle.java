import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {
    private double direction = 0;
    private final double speed = 450;

    public Paddle() {
        super(120, 16);
        setX(Main.WIDTH / 2 - 60);
        setY(Main.HEIGHT - 50);
        setArcWidth(10);
        setArcHeight(10);
        setFill(Color.DODGERBLUE);
    }

    public void setDirection(double newDirection) {
        direction = newDirection;
    }

    public void update(double dt) {
        double newX = getX() + direction * speed * dt;

        if (newX < 0) {
            newX = 0;
        }
        if (newX + getWidth() > Main.WIDTH) {
            newX = Main.WIDTH - getWidth();
        }

        setX(newX);
    }
}