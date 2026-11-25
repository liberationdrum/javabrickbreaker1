import java.util.Random;

public class LevelGenerator {
    public static final int COLUMNS = 10;
    public static final int ROWS = 8;

    public static char[][] generate(int level, long seed) {
        Random random = new Random(seed + level);
        char[][] grid = new char[ROWS][COLUMNS];

        // Start with empty positions.
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                grid[r][c] = '.';
            }
        }

        int numberOfRows = 3 + level;
        if (numberOfRows > ROWS) {
            numberOfRows = ROWS;
        }

        for (int r = 0; r < numberOfRows; r++) {
            for (int c = 0; c < COLUMNS / 2; c++) {
                if (random.nextDouble() < 0.65) {
                    char type = '1';

                    double chance = random.nextDouble();
                    if (chance < 0.10 + level * 0.03) {
                        type = '3';
                    } else if (level >= 2 && chance < 0.15) {
                        type = 'E';
                    } else if (level >= 3 && chance < 0.20) {
                        type = 'X';
                    }

                    // Do not put an unbreakable brick on the bottom row.
                    if (type == 'X' && r == numberOfRows - 1) {
                        type = '1';
                    }

                    grid[r][c] = type;
                    grid[r][COLUMNS - 1 - c] = type;
                }
            }
        }

        // Make sure the level has at least one breakable brick.
        boolean hasBreakable = false;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                if (grid[r][c] == '1' || grid[r][c] == '3' || grid[r][c] == 'E') {
                    hasBreakable = true;
                }
            }
        }

        if (!hasBreakable) {
            grid[0][0] = '1';
            grid[0][COLUMNS - 1] = '1';
        }

        return grid;
    }
}