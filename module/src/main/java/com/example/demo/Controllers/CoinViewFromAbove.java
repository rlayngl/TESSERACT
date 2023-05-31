package com.example.demo.Controllers;

import com.example.demo.Controllers.SpriteAnimations.SpriteAnimationCoin;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class CoinViewFromAbove extends Pane {
    public ImageView imageView;
    public static final int columns = 8;
    public static int offsetX = 0;
    public static final int width = 60;
    public static final int height = 60;
    public static SpriteAnimationCoin spriteAnimation;

    public CoinViewFromAbove(ImageView imageView) {
        this.imageView = imageView;
        this.imageView.setViewport(new Rectangle2D(offsetX, 0, width, height));
        spriteAnimation = new SpriteAnimationCoin(
                imageView, Duration.millis(750), columns, offsetX, width, height);
        getChildren().add(imageView);
        this.setTranslateX(2920);
        this.setTranslateY(770);
    }
}
