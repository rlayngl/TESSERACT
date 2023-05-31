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
    public static Pane appRoot;
    public static Pane gameRoot;
    public static Pane extraGameRoot;
    public static Hero hero;
    public static HeroViewFromAbove heroViewFromAbove;
    public static Coin coin;
    public static CoinViewFromAbove coinViewFromAbove;
    public static ImageView backgroundSkyImage;
    public static ImageView backgroundGroundImage;
    public static List<Obstacle> listOfObstacles;
    public static List<ObstacleViewFromAbove> listOfObstaclesViewFromAbove;
    public static List<Integer> list;
    public static Integer currentLevel;
//    public static List<String> listOfLevels = new ArrayList<>();

    public static Parent createContent() {
        backgroundSkyImage.setFitHeight(700);
        backgroundSkyImage.setFitWidth(3000);
        backgroundGroundImage.setFitHeight(865);
        backgroundGroundImage.setFitWidth(3000);
        gameRoot.setPrefSize(700, 700);
        gameRoot.getChildren().addAll(backgroundSkyImage, hero, coin);
        extraGameRoot.setPrefSize(700, 700);
        extraGameRoot.getChildren().addAll(backgroundGroundImage, heroViewFromAbove, coinViewFromAbove);
        extraGameRoot.setVisible(false);
        ImageView imageViewHero = new ImageView();
        imageViewHero.setViewport(new Rectangle2D(Hero.offsetX, Hero.offsetY, Hero.WIDTH, Hero.HEIGHT));
        Animation animationHero = new SpriteAnimationHero(
                imageViewHero, Duration.millis(500),
                Hero.count, Hero.columns, Hero.offsetX,
                Hero.offsetY, Hero.WIDTH, Hero.HEIGHT
        );
        animationHero.setCycleCount(Animation.INDEFINITE);
        animationHero.play();
        ImageView imageViewHeroAbove = new ImageView();
        imageViewHeroAbove.setViewport(new Rectangle2D(Hero.offsetX, Hero.offsetY, Hero.WIDTH, Hero.HEIGHT));
        Animation animationHeroViewFromAbove = new SpriteAnimationHero(
                imageViewHero, Duration.millis(500),
                Hero.count, Hero.columns, Hero.offsetX,
                Hero.offsetY, Hero.WIDTH, Hero.HEIGHT
        );
        animationHeroViewFromAbove.setCycleCount(Animation.INDEFINITE);
        animationHeroViewFromAbove.play();
        ImageView imageViewCoin = new ImageView();
        imageViewCoin.setViewport(new Rectangle2D(Coin.offsetX, 0, Coin.width, Coin.height));
        Animation animationCoin = new SpriteAnimationCoin(
                imageViewCoin, Duration.millis(500),
                Coin.columns, Coin.offsetX, Coin.width, Coin.height
        );
        animationCoin.setCycleCount(Animation.INDEFINITE);
        animationCoin.play();
        gameRoot.getChildren().addAll(listOfObstacles);
        extraGameRoot.getChildren().addAll(listOfObstaclesViewFromAbove);
        appRoot.getChildren().addAll(gameRoot, extraGameRoot);
        return appRoot;
    }

    public static boolean canDimensionBeChanged;
    public static Integer coordinate;
    public static Integer coordinateXAbove;
    public static Integer coordinateYAbove;

    public static void update() throws IOException {
        if (!isPaused && !isFinished && !isDimensionChanged && !canPauseBeReleased && !stop) {
            coordinate = (int) hero.getTranslateX();

            Coin.spriteAnimation.play();
            if (hero.velocity.getY() < 5) {
                hero.velocity = hero.velocity.add(0, 1);
            }
            hero.moveX((int) hero.velocity.getX());
            hero.moveY((int) hero.velocity.getY());

            canDimensionBeChanged = true;
            listOfObstacles.forEach(e -> {
                int coordinateXOfObstacles = (int) e.getTranslateX();
                if (e.width != 3000 && coordinate + Hero.WIDTH >= coordinateXOfObstacles
                        && coordinate <= coordinateXOfObstacles + e.width) canDimensionBeChanged = false;
            });

            if (isPressed(KeyCode.SPACE)) Hero.jump();
            if (isPressed(KeyCode.A)) {
                hero.moveX(-5);
                Hero.spriteAnimation.setOffsetYHero(141);
                Hero.spriteAnimation.play();
            } else if (isPressed(KeyCode.D)) {
                hero.moveX(5);
                Hero.spriteAnimation.setOffsetYHero(212);
                Hero.spriteAnimation.play();
            } else if (isPressed(KeyCode.R) && canDimensionBeChanged) {
                Hero.spriteAnimation.stop();
                Coin.spriteAnimation.stop();
                changeDimension();
            } else Hero.spriteAnimation.stop();
            if (isPressed(KeyCode.P)) {
                pause();
                Hero.spriteAnimation.stop();
                Coin.spriteAnimation.stop();
            }
            if (coordinate + 58 >= coin.getTranslateX()) {
                finish();
                Hero.spriteAnimation.stop();
                Coin.spriteAnimation.stop();
            }
        }
        if (!isPaused && !isFinished && isDimensionChanged && !canPauseBeReleased && !stop) {
            coordinateXAbove = (int) heroViewFromAbove.getTranslateX();
            coordinateYAbove = (int) heroViewFromAbove.getTranslateY();
            CoinViewFromAbove.spriteAnimation.play();
            heroViewFromAbove.moveX((int) heroViewFromAbove.velocity.getX());
            heroViewFromAbove.moveY((int) heroViewFromAbove.velocity.getY());

            canDimensionBeChanged = true;
            listOfObstaclesViewFromAbove.forEach(e -> {
                int coordinateXOfObstacleAbove = (int) e.getTranslateX();
                if (coordinateXAbove + Hero.WIDTH >= coordinateXOfObstacleAbove
                        && coordinateXAbove <= coordinateXOfObstacleAbove + e.width) {
                    canDimensionBeChanged = false;
                }
            });

            if (isPressed(KeyCode.A)) {
                heroViewFromAbove.moveX(-5);
                HeroViewFromAbove.spriteAnimation.setOffsetYHeroViewFromAbove(141);
                HeroViewFromAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.D)) {
                heroViewFromAbove.moveX(5);
                HeroViewFromAbove.spriteAnimation.setOffsetYHeroViewFromAbove(212);
                HeroViewFromAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.W)) {
                heroViewFromAbove.moveY(-5);
                HeroViewFromAbove.spriteAnimation.setOffsetYHeroViewFromAbove(71);
                HeroViewFromAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.S)) {
                heroViewFromAbove.moveY(5);
                HeroViewFromAbove.spriteAnimation.setOffsetYHeroViewFromAbove(0);
                HeroViewFromAbove.spriteAnimation.play();
            } else if (isPressed(KeyCode.R) && canDimensionBeChanged) {
                HeroViewFromAbove.spriteAnimation.stop();
                Coin.spriteAnimation.stop();
                changeDimensionBack();
            } else HeroViewFromAbove.spriteAnimation.stop();
            if (isPressed(KeyCode.P)) {
                pause();
                HeroViewFromAbove.spriteAnimation.stop();
                Coin.spriteAnimation.stop();
            }
            if (coordinateXAbove >= coinViewFromAbove.getTranslateX() - 30 &&
                    heroViewFromAbove.getTranslateY() >= coinViewFromAbove.getTranslateY() - 30) {
                finish();
                HeroViewFromAbove.spriteAnimation.stop();
                CoinViewFromAbove.spriteAnimation.stop();
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

    public static HashMap<KeyCode, Boolean> map = new HashMap<>();

    public static boolean isPressed(KeyCode key) {
        return map.getOrDefault(key, false);
    }
    public static AnimationTimer timer;
    public static void start(Stage primaryStage) {
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

    public static Rectangle blackScreen = new Rectangle(3000, 1000, Color.BLACK);
    public static Rectangle darkMode = new Rectangle(3000, 1000, Color.BLACK);
    public static boolean isPaused = false;
    public static boolean canPauseBeReleased = false;
    public static boolean isFinished = false;
    public static VBox vBox;

    public static void pause() {
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
    public static void finish() {
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
            if (currentLevel == 0) Level1.start(stage);
            else if (currentLevel == 1) Level2.start(stage);
            else if (currentLevel == 2) Level3.start(stage);
            else if (currentLevel == 3) Level4.start(stage);
            isFinished = false;
        });
    }

    public static Button createButton(String text) {
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

    public static boolean isDimensionChanged = false;
    public static boolean stop = false;

    public static void changeDimension() {
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

    public static void changeDimensionBack() {
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

    public static Stage newStage() {
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
    public static void beginning() {
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


//    public static void levelCompleted(List<String> listYesNo, int number, String replacement) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader("completedLevels.txt"));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            listYesNo.add(line);
//        }
//        listYesNo.set(number, replacement);
//        BufferedWriter writer = new BufferedWriter(new FileWriter("completedLevels.txt"));
//        int count = 0;
//        for (String element : listYesNo) {
//            writer.write(element);
//            count++;
//            if (count != listYesNo.size()) writer.newLine();
//        }
//        writer.close();
//        reader.close();
//        listOfLevels = listYesNo;
//    }
}