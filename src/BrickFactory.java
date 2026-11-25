public class BrickFactory {
    public static Brick create(char code, double x, double y) {
        if (code == '1') {
            return new Brick(x, y, 1, 10);
        } else if (code == '3') {
            return new Brick(x, y, 3, 30);
        } else if (code == 'E') {
            return new ExplosiveBrick(x, y);
        } else if (code == 'X') {
            return new UnbreakableBrick(x, y);
        }

        // A dot means that this position has no brick.
        return null;
    }
}
