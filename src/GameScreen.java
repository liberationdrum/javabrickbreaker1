import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameScreen {
    private Stage stage;
    private boolean endless;

    private Pane root = new Pane();
    private Scene scene;

    private Paddle paddle = new Paddle();
    private Ball ball = new Ball();
    private ArrayList<Brick> bricks = new ArrayList<>();

    private Label scoreLabel = new Label();
    private Label livesLabel = new Label();
    private Label levelLabel = new Label();
    private Label message = new Label();

    private int score = 0;
    private int lives = 3;
    private int level = 1;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean ready = true;
    private boolean paused = false;
    private boolean gameOver = false;
    private boolean won = false;

    private long seed = System.currentTimeMillis();
    private AnimationTimer timer;

    public GameScreen(Stage stage, boolean endless) {
        this.stage = stage;
        this.endless = endless;

        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        root.setStyle("-fx-background-color: #101020;");

        root.getChildren().add(paddle);
        root.getChildren().add(ball);

        makeHud();
        makeMessage();
        buildLevel();
        startRound();
        setupKeys();
        startGameLoop();
        changeBackground();
    }

    public Scene getScene() {
        return scene;
    }

    private void makeHud() {
        scoreLabel.setTextFill(Color.WHITE);
        livesLabel.setTextFill(Color.WHITE);
        levelLabel.setTextFill(Color.WHITE);

        scoreLabel.setFont(Font.font(18));
        livesLabel.setFont(Font.font(18));
        levelLabel.setFont(Font.font(18));

        HBox hud = new HBox(40);
        hud.getChildren().addAll(scoreLabel, livesLabel, levelLabel);
        hud.setLayoutX(10);
        hud.setLayoutY(8);

        root.getChildren().add(hud);
        updateHud();
    }

    private void updateHud() {
        scoreLabel.setText("Score: " + score);
        livesLabel.setText("Lives: " + lives);
        levelLabel.setText("Level: " + level);
    }

    private void makeMessage() {
        message.setTextFill(Color.WHITE);
        message.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        message.setTextAlignment(TextAlignment.CENTER);
        message.setAlignment(Pos.CENTER);
        message.setPrefWidth(Main.WIDTH);
        message.setLayoutY(Main.HEIGHT / 2);
        message.setMouseTransparent(true);

        root.getChildren().add(message);
    }

    private void showMessage(String text) {
        message.setText(text);
        message.setVisible(true);
    }

    private void hideMessage() {
        message.setVisible(false);
    }

    private void buildLevel() {
        root.getChildren().removeAll(bricks);
        bricks.clear();

        char[][] grid;

        if (!endless && level <= 3) {
            try {
                grid = LevelLoader.load("/levels/level" + level + ".txt");
            } catch (Exception e) {
                grid = LevelGenerator.generate(level, seed);
            }
        } else {
            grid = LevelGenerator.generate(level, seed);
        }

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                double x = c * (Brick.WIDTH + 2) + 1;
                double y = Main.TOP + 20 + r * (Brick.HEIGHT + 2);

                Brick brick = BrickFactory.create(grid[r][c], x, y);

                if (brick != null) {
                    bricks.add(brick);
                    root.getChildren().add(brick);
                }
            }
        }
    }

    private void setupKeys() {
        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();

            if (key == KeyCode.LEFT || key == KeyCode.A) {
                leftPressed = true;
            }

            if (key == KeyCode.RIGHT || key == KeyCode.D) {
                rightPressed = true;
            }

            if (key == KeyCode.SPACE && ready && !gameOver) {
                ready = false;
                ball.launch();
                hideMessage();
            }

            if (key == KeyCode.P && !gameOver && !won) {
                paused = !paused;

                if (paused) {
                    showMessage("PAUSED - Press P to continue");
                } else {
                    hideMessage();
                }
            }

            if (key == KeyCode.ENTER && won) {
                nextLevel();
            }

            if (key == KeyCode.R && gameOver) {
                restart();
            }

            if (key == KeyCode.ESCAPE) {
                timer.stop();
                stage.setScene(MenuScreen.create(stage));
            }
        });

        scene.setOnKeyReleased(event -> {
            KeyCode key = event.getCode();

            if (key == KeyCode.LEFT || key == KeyCode.A) {
                leftPressed = false;
            }

            if (key == KeyCode.RIGHT || key == KeyCode.D) {
                rightPressed = false;
            }
        });
    }

    private void startGameLoop() {
        timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double dt = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                // Prevent a very large movement after the computer lags.
                if (dt > 0.033) {
                    dt = 0.033;
                }

                updateGame(dt);
            }
        };

        timer.start();
    }

    private void updateGame(double dt) {
        if (paused || gameOver || won) {
            return;
        }

        double direction = 0;

        if (leftPressed) {
            direction = -1;
        }
        if (rightPressed) {
            direction = 1;
        }
        if (leftPressed && rightPressed) {
            direction = 0;
        }

        paddle.setDirection(direction);
        paddle.update(dt);

        if (ready) {
            ball.stickTo(paddle);
            return;
        }

        ball.update(dt);
        ball.bounceOff(paddle);
        checkBrickCollision();

        if (ball.getCenterY() - ball.getRadius() > Main.HEIGHT) {
            loseLife();
        }

        if (allBreakableBricksGone()) {
            won = true;
            showMessage("LEVEL CLEARED! Press ENTER");
        }
    }

    private void checkBrickCollision() {
        // Use a copy because a brick can be removed during this loop.
        ArrayList<Brick> copy = new ArrayList<>(bricks);

        for (Brick brick : copy) {
            if (ball.getBoundsInParent().intersects(brick.getBoundsInParent())) {
                ball.bounceOff(brick);

                boolean destroyed = brick.hit();

                if (destroyed) {
                    destroyBrick(brick);

                    if (brick instanceof ExplosiveBrick) {
                        explode(brick);
                    }
                }

                // Handle only one brick in each frame.
                break;
            }
        }
    }

    private void destroyBrick(Brick brick) {
        bricks.remove(brick);
        root.getChildren().remove(brick);

        score = score + brick.getPoints();
        updateHud();
    }

    private void explode(Brick center) {
        ArrayList<Brick> copy = new ArrayList<>(bricks);

        for (Brick other : copy) {
            double distanceX = Math.abs(other.getX() - center.getX());
            double distanceY = Math.abs(other.getY() - center.getY());

            if (distanceX <= Brick.WIDTH + 4 &&
                    distanceY <= Brick.HEIGHT + 4 &&
                    other.isBreakable()) {

                destroyBrick(other);
            }
        }
    }

    private boolean allBreakableBricksGone() {
        for (Brick brick : bricks) {
            if (brick.isBreakable()) {
                return false;
            }
        }
        return true;
    }

    private void startRound() {
        ready = true;
        paused = false;
        ball.stickTo(paddle);
        showMessage("Press SPACE to launch");
    }

    private void loseLife() {
        lives--;
        updateHud();

        if (lives <= 0) {
            gameOver = true;
            HighScoreManager.add(score);
            showMessage("GAME OVER\nScore: " + score + "\nPress R to restart");
        } else {
            startRound();
        }
    }

    private void nextLevel() {
        level++;
        won = false;

        double newSpeed = Ball.BASE_SPEED + (level - 1) * 20;
        if (newSpeed > 560) {
            newSpeed = 560;
        }

        ball.setSpeed(newSpeed);
        buildLevel();
        updateHud();
        changeBackground();
        startRound();
    }

    private void restart() {
        score = 0;
        lives = 3;
        level = 1;

        gameOver = false;
        won = false;
        paused = false;

        ball.setSpeed(Ball.BASE_SPEED);
        buildLevel();
        updateHud();
        changeBackground();
        startRound();
    }

    private void changeBackground() {
        Theme theme = Theme.forLevel(level);
        String file = theme.name().toLowerCase();

        Image image = AssetLoader.loadImage("/images/backgrounds/" + file + ".png");

        if (image != null) {
            BackgroundImage background = new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true)
            );

            root.setBackground(new Background(background));
        } else {
            root.setStyle("-fx-background-color: #101020;");
        }
    }
}