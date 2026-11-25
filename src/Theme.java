public enum Theme {
    SPACE, RETRO, NEON;

    public static Theme forLevel(int level) {
        int index = ((level - 1) / 5) % 3;
        return values()[index];
    }
}