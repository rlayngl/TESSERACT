package com.example.demo.Controllers;

import com.example.demo.Controllers.SpriteAnimations.SpriteAnimationHero;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import static com.example.demo.Controllers.LevelController.hero;


public class Hero extends Pane {
    public Point2D velocity = new Point2D(0, 0);
    public final ImageView imageView;
    public static final int columns = 4;
    public static final int count = 4;
    public static int offsetX = 0;
    public static int offsetY = 0;
    public static final int WIDTH = 58;
    public static final int HEIGHT = 71;
    public static boolean canJump = true;
    public static SpriteAnimationHero spriteAnimation;

    public Hero(ImageView imageView) {
            this.imageView = imageView;
            this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, WIDTH, HEIGHT));
            spriteAnimation = new SpriteAnimationHero(
                    imageView, Duration.millis(500), count, columns, offsetX, offsetY, WIDTH, HEIGHT);
            getChildren().add(imageView);
            this.setTranslateX(10);
            this.setTranslateY(480);
    }
    public void moveY (int value) {
        boolean moveDown = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            for (Obstacle obstacle : LevelController.listOfObstacles) {
                if (this.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                    if (moveDown) {
                        setTranslateY(getTranslateY() - 1);
                        canJump = true;
                    } else {
                        setTranslateY(getTranslateY() + 2);
                    }
                    return;
                } else canJump = false;
            }
            if (getTranslateY() < 0) setTranslateY(0);
            if (getTranslateX() > 3000 - WIDTH) setTranslateX(3000 - WIDTH);
            if (getTranslateY() > 700) setTranslateY(700);
            this.setTranslateY(getTranslateY() + (moveDown ? 1 : -1));
        }
    }

        public void moveX (int value) {
        boolean rightMove = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            for (Obstacle obstacle : LevelController.listOfObstacles) {
                if (this.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                    if (getTranslateX() + WIDTH == obstacle.getTranslateX() && rightMove) {
                        setTranslateX(getTranslateX() - 1);
                    } else if (getTranslateX() == obstacle.getTranslateX() + obstacle.width && !rightMove) {
                        setTranslateX(getTranslateX() + 1);
                    }
                    return;
                }
            }
            if (getTranslateX() < 0) setTranslateX(0);
            this.setTranslateX(getTranslateX() + (rightMove ? 1 : -1));
        }
    }
        public static void jump () {
        if (canJump) {
            hero.velocity = hero.velocity.add(0, -20);
            canJump = false;
        }
    }
}
