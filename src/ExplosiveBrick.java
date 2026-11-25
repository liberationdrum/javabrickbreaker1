import javafx.scene.paint.Color;

public class ExplosiveBrick extends Brick {
    public ExplosiveBrick(double x, double y) {
        super(x, y, 1, 50);
        setFill(Color.GOLD);
    }

    @Override
    protected void updateColor() {
        setFill(Color.GOLD);
    }
}