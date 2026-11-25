import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoreManager {
    private static final String FILE_NAME = "scores.txt";

    public static ArrayList<Integer> load() {
        ArrayList<Integer> scores = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    scores.add(Integer.parseInt(line));
                } catch (NumberFormatException e) {
                    // Ignore a line that is not a number.
                }
            }

            reader.close();
        } catch (IOException e) {
            // The file may not exist yet. That is okay.
        }

        Collections.sort(scores, Collections.reverseOrder());
        return scores;
    }

    public static void add(int score) {
        ArrayList<Integer> scores = load();
        scores.add(score);
        Collections.sort(scores, Collections.reverseOrder());

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));

            for (int i = 0; i < scores.size() && i < 10; i++) {
                writer.println(scores.get(i));
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Could not save the score.");
        }
    }
}