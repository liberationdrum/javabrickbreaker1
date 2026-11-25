import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    public static final double WIDTH = 800;
    public static final double HEIGHT = 600;
    public static final double TOP = 40;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Brick Breaker");
        stage.setScene(MenuScreen.create(stage));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}