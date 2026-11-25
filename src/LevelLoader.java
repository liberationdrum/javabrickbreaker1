import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LevelLoader {
    public static char[][] load(String fileName) throws IOException {
        InputStream input = LevelLoader.class.getResourceAsStream(fileName);

        if (input == null) {
            throw new IOException("Level file was not found: " + fileName);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        ArrayList<char[]> rows = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            rows.add(line.toCharArray());
        }

        reader.close();

        char[][] grid = new char[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            grid[i] = rows.get(i);
        }

        return grid;
    }
}