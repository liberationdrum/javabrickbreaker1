import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends Circle {
    public static final double BASE_SPEED = 320;
    private double dx = 0;
    private double dy = 0;
    private double speed = BASE_SPEED;

    public Ball() {
        super(8, Color.WHITE);
    }

    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }

    public void stickTo(Paddle paddle) {
        setCenterX(paddle.getX() + paddle.getWidth() / 2);
        setCenterY(paddle.getY() - getRadius() - 1);
        dx = 0;
        dy = 0;
    }

    public void launch() {
        double angle = Math.toRadians(Math.random() * 40 - 20);
        dx = speed * Math.sin(angle);
        dy = -speed * Math.cos(angle);
    }

    public void update(double dt) {
        double r = getRadius();

        setCenterX(getCenterX() + dx * dt);
        setCenterY(getCenterY() + dy * dt);

        if (getCenterX() - r < 0) {
            setCenterX(r);
            dx = Math.abs(dx);
        }

        if (getCenterX() + r > Main.WIDTH) {
            setCenterX(Main.WIDTH - r);
            dx = -Math.abs(dx);
        }

        if (getCenterY() - r < Main.TOP) {
            setCenterY(Main.TOP + r);
            dy = Math.abs(dy);
        }
    }

    public void bounceOff(Paddle paddle) {
        if (dy <= 0) {
            return;
        }

        if (!getBoundsInParent().intersects(paddle.getBoundsInParent())) {
            return;
        }

        double hitPosition = (getCenterX() - paddle.getX()) / paddle.getWidth();

        if (hitPosition < 0) hitPosition = 0;
        if (hitPosition > 1) hitPosition = 1;

        double angle = Math.toRadians((hitPosition - 0.5) * 120);

        dx = speed * Math.sin(angle);
        dy = -speed * Math.cos(angle);
        setCenterY(paddle.getY() - getRadius() - 1);
    }

    public void bounceOff(Brick brick) {
        double overlapX = Math.min(
                getCenterX() + getRadius() - brick.getX(),
                brick.getX() + brick.getWidth() - (getCenterX() - getRadius())
        );

        double overlapY = Math.min(
                getCenterY() + getRadius() - brick.getY(),
                brick.getY() + brick.getHeight() - (getCenterY() - getRadius())
        );

        if (overlapX < overlapY) {
            dx = -dx;
        } else {
            dy = -dy;
        }
    }
}