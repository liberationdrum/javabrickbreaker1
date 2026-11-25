import javafx.scene.paint.Color;
public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y) {
        super(x, y, 999, 0);
        setFill(Color.GRAY);
    }

    @Override
    public boolean hit() {
        return false;
    }

    @Override
    public boolean isBreakable() {
        return false;
    }
}