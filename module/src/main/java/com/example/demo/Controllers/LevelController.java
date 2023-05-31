package com.example.demo.Controllers;

import com.example.demo.Application;
import com.example.demo.Controllers.Levels.*;
import com.example.demo.Controllers.SpriteAnimations.SpriteAnimationCoin;
import com.example.demo.Controllers.SpriteAnimations.SpriteAnimationHero;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class LevelController {
    public Pane appRoot;
    public Pane gameRoot;
    public Pane extraGameRoot;
    public ImageView backgroundSkyImage;
    public ImageView backgroundGroundImage;
    public List<Obstacle> listOfObstacles;
    public List<ObstacleViewFromAbove> listOfObstaclesViewFromAbove;
    public List<Integer> list;
    public Integer currentLevel;
    public LevelController levelController;
    public Level1 level1;
    public Level2 level2;
    public Level3 level3;
    public Level4 level4;
    public Hero hero;
    public Coin coin;
    public HeroViewFromAbove heroViewFromAbove;
    public CoinViewFromAbove coinViewFromAbove;
    public LevelController(LevelController levelController) {
        this.levelController = levelController;
    }

    public Parent createContent() {
        backgroundSkyImage.setFitHeight(700);
        backgroundSkyImage.setFitWidth(3000);
        backgroundGroundImage.setFitHeight(865);
        backgroundGroundImage.setFitWidth(3000);
        gameRoot.setPrefSize(700, 700);
        gameRoot.getChildren().addAll(backgroundSkyImage, hero, coin);
        extraGameRoot.setPrefSize(700, 700);
        extraGameRoot.getChildren().addAll(backgroundGroundImage, heroViewFromAbove, coinViewFromAbove);
        extraGameRoot.setVisible(false);
        ImageView imageViewhero = new ImageView();
        imageViewhero.setViewport(new Rectangle2D(hero.offsetX, hero.offsetY, hero.WIDTH, hero.HEIGHT));
        Animation animationhero = new SpriteAnimationHero(
                imageViewhero, Duration.millis(500),
                hero.count, hero.columns, hero.offsetX,
                hero.offsetY, hero.WIDTH, hero.HEIGHT
        );
        animationhero.setCycleCount(Animation.INDEFINITE);
        animationhero.play();
        ImageView imageViewheroAbove = new ImageView();
        imageViewheroAbove.setViewport(new Rectangle2D(hero.offsetX, hero.offsetY, hero.WIDTH, hero.HEIGHT));
        Animation animationheroViewFromAbove = new SpriteAnimationHero(
                imageViewhero, Duration.millis(500),
                hero.count, hero.columns, hero.offsetX,
                hero.offsetY, hero.WIDTH, hero.HEIGHT
        );
        animationheroViewFromAbove.setCycleCount(Animation.INDEFINITE);
        animationheroViewFromAbove.play();
        ImageView imageViewCoin = new ImageView();
        imageViewCoin.setViewport(new Rectangle2D(coin.offsetX, 0, coin.width, coin.height));
        Animation animationCoin = new SpriteAnimationCoin(
                imageViewCoin, Duration.millis(500),
                coin.columns, coin.offsetX, coin.width, coin.height
        );
        animationCoin.setCycleCount(Animation.INDEFINITE);
        animationCoin.play();
        gameRoot.getChildren().addAll(listOfObstacles);
        extraGameRoot.getChildren().addAll(listOfObstaclesViewFromAbove);
        appRoot.getChildren().addAll(gameRoot, extraGameRoot);
        return appRoot;
    }

    public boolean canDimensionBeChanged;
    public Integer coordinate;
    public Integer coordinateXAbove;
    public Integer coordinateYAbove;

    public void update() throws IOException {
        if (!isPaused && !isFinished && !isDimensionChanged && !canPauseBeReleased && !stop) {
            coordinate = (int) hero.getTranslateX();

            coin.spriteAnimation.play();
            if (hero.velocity.getY() < 5) {
                hero.velocity = hero.velocity.add(0, 1);
            }
            hero.moveX((int) hero.velocity.getX());
            hero.moveY((int) hero.velocity.getY());

            canDimensionBeChanged = true;
            listOfObstacles.forEach(e -> {
                int coordinateXOfObstacles = (int) e.getTranslateX();
                if (e.width != 3000 && coordinate + hero.WIDTH >= coordinateXOfObstacles
                        && coordinate <= coordinateXOfObstacles + e.width) canDimensionBeChanged = false;
            });

            if (isPressed(KeyCode.SPACE)) hero.jump();
            if (isPressed(KeyCode.A)) {
                hero.moveX(-5);
                hero.spriteAnimation.setOffsetYhero(141);
                hero.spriteAnimation.play();
            } else if (isPressed(KeyCode.D)) {
                hero.moveX(5);
                hero.spriteAnimation.setOffsetYhero(212);
                hero.spriteAnimation.play();
            } else if (isPressed(KeyCode.R) && canDimensionBeChanged) {
                hero.spriteAnimation.stop();
                coin.spriteAnimation.stop();
                changeDimension();
            } else hero.spriteAnimation.stop();
            if (isPressed(KeyCode.P)) {
                pause();
                hero.spriteAnimation.stop();
                coin.spriteAnimation.stop();
            }
            if (coordinate + 58 >= coin.getTranslateX()) {
                finish();
                hero.spriteAnimation.stop();
                coin.spriteAnimation.stop();
            }
        }
        if (!isPaused && !isFinished && isDimensionChanged && !canPauseBeReleased && !stop) {
            coordinateXAbove = (int) heroViewFromAbove.getTranslateX();
            coordinateYAbove = (int) heroViewFromAbove.getTranslateY();
            coinViewFromAbove.spriteAnimation.play();
            heroViewFromAbove.moveX((int) heroViewFromAbove.velocity.getX());
            heroViewFromAbove.moveY((int) heroViewFromAbove.velocity.getY());

            canDimensionBeChanged = true;
            listOfObstaclesViewFromAbove.forEach(e -> {
                int coordinateXOfObstacleAbove = (int) e.getTranslateX();
                if (coordinateXAbove + hero.WIDTH >= coordinateXOfObstacleAbove
                        && coordinateXAbove <= coordinateXOfObstacleAbove + e.width) {
                    canDimensionBeChanged = false;
                }
            });

            if (isPressed(KeyCode.A)) {
                heroViewFromAbove.moveX(-5);
                heroViewFromAbove.spriteAnimation.setOffsetYheroViewFromAbove(141);
                heroViewFromAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.D)) {
                heroViewFromAbove.moveX(5);
                heroViewFromAbove.spriteAnimation.setOffsetYheroViewFromAbove(212);
                heroViewFromAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.W)) {
                heroViewFromAbove.moveY(-5);
                heroViewFromAbove.spriteAnimation.setOffsetYheroViewFromAbove(71);
                heroViewFromAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.S)) {
                heroViewFromAbove.moveY(5);
                heroViewFromAbove.spriteAnimation.setOffsetYheroViewFromAbove(0);
                heroViewFromAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.R) && canDimensionBeChanged) {
                heroViewFromAbove.spriteAnimation.stop();
                coin.spriteAnimation.stop();
                changeDimensionBack();
            } else heroViewFromAbove.spriteAnimation.stop();
            if (isPressed(KeyCode.P)) {
                pause();
                heroViewFromAbove.spriteAnimation.stop();
                coin.spriteAnimation.stop();
            }
            if (coordinateXAbove >= coinViewFromAbove.getTranslateX() - 30 &&
                    heroViewFromAbove.getTranslateY() >= coinViewFromAbove.getTranslateY() - 30) {
                finish();
                heroViewFromAbove.spriteAnimation.stop();
                coinViewFromAbove.spriteAnimation.stop();
            }
        }
        if (isPaused && canPauseBeReleased) {
            if (isPressed(KeyCode.P)) {
                isPaused = false;
                darkMode.setOpacity(0.7);
                if (!isDimensionChanged) gameRoot.getChildren().remove(vBox);
                if (isDimensionChanged) extraGameRoot.getChildren().remove(vBox);
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), darkMode);
                fadeTransition.setFromValue(0.7);
                fadeTransition.setToValue(0.0);
                fadeTransition.setOnFinished(event -> {
                    if (!isDimensionChanged) gameRoot.getChildren().remove(darkMode);
                    if (isDimensionChanged) extraGameRoot.getChildren().remove(darkMode);
                    canPauseBeReleased = false;
                });
                fadeTransition.play();
            }
        }
        if (isDimensionChanged) {
            int offset = coordinateXAbove;
            if (offset > 350 && offset < 1815) {
                extraGameRoot.setLayoutX(350 - offset);
            } else if (offset >= 1815) extraGameRoot.setLayoutX(-1463);
            else extraGameRoot.setLayoutX(0);
        } else {
            int offset = coordinate;
            if (offset > 350 && offset < 1815) {
                gameRoot.setLayoutX(350 - offset);
            } else if (offset >= 1815) gameRoot.setLayoutX(-1463);
            else gameRoot.setLayoutX(0);
        }
    }

    public HashMap<KeyCode, Boolean> map = new HashMap<>();

    public boolean isPressed(KeyCode key) {
        return map.getOrDefault(key, false);
    }
    public AnimationTimer timer;
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnKeyPressed(event -> map.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> map.put(event.getCode(), false));
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.start();
        if (currentLevel == 0) beginning();
    }

    public Rectangle blackScreen = new Rectangle(3000, 1000, Color.BLACK);
    public Rectangle darkMode = new Rectangle(3000, 1000, Color.BLACK);
    public boolean isPaused = false;
    public boolean canPauseBeReleased = false;
    public boolean isFinished = false;
    public VBox vBox;

    public void pause() {
        isPaused = true;
        canPauseBeReleased = false;
        darkMode.setOpacity(0.0);
        if (!isDimensionChanged) gameRoot.getChildren().add(darkMode);
        if (isDimensionChanged) extraGameRoot.getChildren().add(darkMode);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), darkMode);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(0.7);
        fadeTransition.setOnFinished(event -> {
            vBox = new VBox(20);
            Button buttonContinue = createButton("CONTINUE");
            Button buttonMenu = createButton("MENU");
            Label label = new Label("PAUSE");
            label.setTextFill(Color.GRAY);
            Font font = Font.font("Gill Sans Ultra Bold", 90);
            label.setFont(font);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(label, buttonContinue, buttonMenu);
            if (!isDimensionChanged) {
                if (coordinate < 350) {
                    vBox.setLayoutX(580);
                } else if (coordinate > 1810) {
                    vBox.setLayoutX(2030);
                } else vBox.setLayoutX(coordinate + 230);
            } else {
                if (coordinateXAbove < 350) {
                    vBox.setLayoutX(580);
                } else if (coordinateXAbove > 1810) {
                    vBox.setLayoutX(2030);
                } else vBox.setLayoutX(coordinateXAbove + 230);
            }
            vBox.setLayoutY(250);
            if (!isDimensionChanged) gameRoot.getChildren().add(vBox);
            if (isDimensionChanged) extraGameRoot.getChildren().add(vBox);
            buttonContinue.setOnAction(e -> {
                isPaused = false;
                darkMode.setOpacity(0.7);
                if (!isDimensionChanged) gameRoot.getChildren().remove(vBox);
                if (isDimensionChanged) extraGameRoot.getChildren().remove(vBox);
                FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(300), darkMode);
                fadeTransition2.setFromValue(0.7);
                fadeTransition2.setToValue(0.0);
                fadeTransition2.setOnFinished(event2 -> {
                    if (!isDimensionChanged) gameRoot.getChildren().remove(darkMode);
                    if (isDimensionChanged) extraGameRoot.getChildren().remove(darkMode);
                    canPauseBeReleased = false;
                });
                fadeTransition2.play();
            });
            buttonMenu.setOnAction(e -> {
                isFinished = true;
                appRoot.getScene().getWindow().hide();
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainMenu.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 700, 500);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Stage stage = newStage();
                stage.setScene(scene);
                stage.show();
                isPaused = false;
                canPauseBeReleased = false;
                isFinished = false;
            });
            canPauseBeReleased = true;
        });
        fadeTransition.play();
    }
    public void finish() {
        isFinished = true;
        timer.stop();
        Button buttonNextLevel = createButton("NEXT LEVEL");
        Button buttonMenu = createButton("MENU");
        Label label = new Label("CONGRATS!");
        label.setTextFill(Color.GRAY);
        Font font = Font.font("Gill Sans Ultra Bold", 90);
        label.setFont(font);
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        if (currentLevel != 4) {
            vBox.getChildren().addAll(label, buttonNextLevel, buttonMenu);
            vBox.setLayoutY(250);
        } else {
            vBox.getChildren().addAll(label, buttonMenu);
            vBox.setLayoutY(330);
        }
        vBox.setLayoutX(1900);
        blackScreen.setOpacity(0.7);
        if (!isDimensionChanged) {
            gameRoot.getChildren().addAll(blackScreen, vBox);
        } else {
            extraGameRoot.getChildren().addAll(blackScreen, vBox);
        }
        buttonMenu.setOnAction(e -> {
            appRoot.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainMenu.fxml"));
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 500);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Stage stage = newStage();
            stage.setScene(scene);
            stage.show();
            isFinished = false;
        });
        buttonNextLevel.setOnAction(e -> {
            Stage stage = newStage();
            if (currentLevel == 0) {
                level1 = new Level1();
                level1.start(stage);
            }
            else if (currentLevel == 1) {
                level2 = new Level2();
                level2.start(stage);
            }
            else if (currentLevel == 2) {
                level3 = new Level3();
                level3.start(stage);
            }
            else if (currentLevel == 3) {
                level4 = new Level4();
                level4.start(stage);
            }
            isFinished = false;
        });
    }

    public Button createButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(300);
        button.setPrefHeight(100);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: gray;" +
                " -fx-border-WIDTH: 2px; -fx-border-radius: 25;");
        button.setTextFill(Color.GRAY);
        Font font = Font.font("Gill Sans Ultra Bold", 32);
        button.setFont(font);
        return button;
    }

    public boolean isDimensionChanged = false;
    public boolean stop = false;

    public void changeDimension() {
        stop = true;
        darkMode.setOpacity(0);
        gameRoot.getChildren().remove(darkMode);
        gameRoot.getChildren().add(darkMode);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), darkMode);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        heroViewFromAbove.setTranslateX(coordinate);
        fadeTransition.setOnFinished(event -> {
            gameRoot.setVisible(false);
            extraGameRoot.setVisible(true);
            stop = false;
            isDimensionChanged = true;
        });
        fadeTransition.play();
    }

    public void changeDimensionBack() {
        stop = true;
        darkMode.setOpacity(0);
        extraGameRoot.getChildren().remove(darkMode);
        extraGameRoot.getChildren().add(darkMode);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), darkMode);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        hero.setTranslateX(coordinateXAbove);
        fadeTransition.setOnFinished(event -> {
            extraGameRoot.setVisible(false);
            gameRoot.setVisible(true);
            stop = false;
            isDimensionChanged = false;
        });
        fadeTransition.play();
    }

    public Stage newStage() {
        timer.stop();
        gameRoot.getChildren().clear();
        extraGameRoot.getChildren().clear();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.setFullScreen(true);
        stage.getIcons().add(new Image("Images/Icon.png"));
        appRoot.getScene().getWindow().hide();
        isDimensionChanged = false;
        list.clear();
        listOfObstacles.clear();
        listOfObstaclesViewFromAbove.clear();
        appRoot.getChildren().clear();
        return stage;
    }
    public void beginning() {
        timer.stop();
        isPaused = true;
        darkMode.setOpacity(0.0);
        gameRoot.getChildren().add(darkMode);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), darkMode);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(0.7);
        fadeTransition.setOnFinished(event -> {
            vBox = new VBox(20);
            Button buttonOK = createButton("OK");
            Label label = new Label("CONTROLS:");
            Label text = new Label("""
                                            To begin you need to know some basic control keys:
                    To move right - press D; to move left - A, to move forward - W, to move back - S.
                    If you want your character to jump - press SPACE. P is responsible for the pause.
                      Secret key which will help you to finish the game is R. It changes dimensions.
                                                          Now you know everything. Good luck!""");
            label.setTextFill(Color.GRAY);
            text.setTextFill(Color.GRAY);
            Font font = Font.font("Gill Sans Ultra Bold", 90);
            Font font2 = Font.font("Leelawadee Bold", 32);
            label.setFont(font);
            text.setFont(font2);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(label, text, buttonOK);
            vBox.setLayoutY(230);
            vBox.setLayoutX(140);
            gameRoot.getChildren().add(vBox);
            buttonOK.setOnAction(e -> {
                darkMode.setOpacity(0.7);
                gameRoot.getChildren().remove(vBox);
                FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(300), darkMode);
                fadeTransition2.setFromValue(0.7);
                fadeTransition2.setToValue(0.0);
                fadeTransition2.setOnFinished(event2 -> {
                    gameRoot.getChildren().remove(darkMode);
                    timer.start();
                    isPaused = false;
                });
                fadeTransition2.play();
            });
        });
        fadeTransition.play();
    }
}