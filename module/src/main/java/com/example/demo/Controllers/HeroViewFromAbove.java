package com.example.demo.Controllers;

import com.example.demo.Controllers.SpriteAnimations.SpriteAnimationHeroViewFromAbove;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class HeroViewFromAbove extends Pane {
    public Point2D velocity = new Point2D(0, 0);
    public final ImageView imageView;
    public static final int columns = 4;
    public static final int count = 4;
    public static int offsetX = 0;
    public static int offsetY = 0;
    public static final int WIDTH = 58;
    public static final int HEIGHT = 71;
    public static SpriteAnimationHeroViewFromAbove spriteAnimation;

    public HeroViewFromAbove(ImageView imageView) {
        this.imageView = imageView;
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, WIDTH, HEIGHT));
        spriteAnimation = new SpriteAnimationHeroViewFromAbove(
                imageView, Duration.millis(500), count, columns, offsetX, offsetY, WIDTH, HEIGHT);
        getChildren().add(imageView);
        this.setTranslateX(10);
        this.setTranslateY(700);
    }

    public void moveX(int value) {
        boolean rightMove = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            for (ObstacleViewFromAbove obstacle : LevelController.listOfObstaclesViewFromAbove) {
                if (this.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                    if (getTranslateX() + WIDTH >= obstacle.getTranslateX() && rightMove) {
                        setTranslateX(getTranslateX() - 1);
                    } else if (getTranslateX() == obstacle.getTranslateX() + obstacle.width && !rightMove) {
                        setTranslateX(getTranslateX() + 1);
                    }
                    return;
                }
            }
            if (getTranslateX() < 0) setTranslateX(0);
            if (getTranslateX() > 3000 - WIDTH) setTranslateX(3000 - WIDTH);
            this.setTranslateX(getTranslateX() + (rightMove ? 1 : -1));
        }
    }
    public void moveY(int value){
        boolean forwardMove = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            for (ObstacleViewFromAbove obstacle : LevelController.listOfObstaclesViewFromAbove) {
                if (this.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                    if (getTranslateY() + HEIGHT == obstacle.getTranslateY() && forwardMove) {
                        setTranslateY(getTranslateY() - 1);
                    } else if (getTranslateY() == obstacle.getTranslateY() + obstacle.length && !forwardMove) {
                        setTranslateY(getTranslateY() + 1);
                    }
                    return;
                }
            }
            if (getTranslateY() < 0) setTranslateY(0);
            if (getTranslateY() > 800) setTranslateY(800);
            this.setTranslateY(getTranslateY() + (forwardMove ? 1 : -1));
        }
    }
}