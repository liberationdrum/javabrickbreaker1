import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MenuScreen {
    public static Scene create(Stage stage) {
        Label title = new Label("BRICK BREAKER");
        title.setFont(Font.font(42));

        Button campaign = new Button("Campaign");
        Button endless = new Button("Endless");
        Button scores = new Button("High Scores");
        Button exit = new Button("Exit");

        campaign.setPrefWidth(220);
        endless.setPrefWidth(220);
        scores.setPrefWidth(220);
        exit.setPrefWidth(220);

        campaign.setOnAction(e -> {
            GameScreen game = new GameScreen(stage, false);
            stage.setScene(game.getScene());
        });

        endless.setOnAction(e -> {
            GameScreen game = new GameScreen(stage, true);
            stage.setScene(game.getScene());
        });

        scores.setOnAction(e -> showScores());
        exit.setOnAction(e -> Platform.exit());

        VBox box = new VBox(15);
        box.getChildren().addAll(title, campaign, endless, scores, exit);
        box.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(box);
        root.setStyle("-fx-background-color: #101020;");

        return new Scene(root, Main.WIDTH, Main.HEIGHT);
    }

    private static void showScores() {
        ArrayList<Integer> scores = HighScoreManager.load();
        String text = "";

        for (int i = 0; i < scores.size(); i++) {
            text = text + (i + 1) + ". " + scores.get(i) + "\n";
        }

        if (text.isEmpty()) {
            text = "No scores yet.";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("High Scores");
        alert.setHeaderText("Best Scores");
        alert.setContentText(text);
        alert.showAndWait();
    }
}