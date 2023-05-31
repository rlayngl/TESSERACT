package com.example.demo.Controllers;

import com.example.demo.Controllers.Levels.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private Button YesButton;
    @FXML
    private Pane PlayPane;
    @FXML
    private Pane SettingsPane;
    @FXML
    private Pane InformationPane;
    @FXML
    private Pane QuitPane;
    @FXML
    private Button Level0Button;
    @FXML
    private Button Level1Button;
    @FXML
    private Button Level2Button;
    @FXML
    private Button Level3Button;
    @FXML
    private Button Level4Button;
    @FXML
    protected void Play() {
        PlayPane.setVisible(true);
    }
    @FXML
    protected void Settings() {
        SettingsPane.setVisible(true);
    }
    @FXML
    protected void Information() {
        InformationPane.setVisible(true);
    }
    @FXML
    protected void Quit() {
        QuitPane.setVisible(true);
    }
    @FXML
    protected void Yes() {
        YesButton.getScene().getWindow().hide();
    }
    @FXML
    protected void BackPlay() {
        PlayPane.setVisible(false);
    }
    @FXML
    protected void BackSettings() {
        SettingsPane.setVisible(false);
    }
    @FXML
    protected void BackInformation() {
        InformationPane.setVisible(false);
    }
    @FXML
    protected void No() {
        QuitPane.setVisible(false);
    }
    Level0 level0;
    Level1 level1;
    Level2 level2;
    Level3 level3;
    Level4 level4;
    @FXML
    protected void PlayLevel0() {
        Level0Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        level0 = new Level0();
        level0.start(stage);
    }
    @FXML
    protected void PlayLevel1() {
        Level1Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        level1 = new Level1();
        level1.start(stage);
    }
    @FXML
    protected void PlayLevel2() {
        Level2Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        level2 = new Level2();
        level2.start(stage);
    }
    @FXML
    protected void PlayLevel3() {
        Level3Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        level3 = new Level3();
        level3.start(stage);
    }
    @FXML
    protected void PlayLevel4() {
        Level4Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        stage.show();
        level4 = new Level4();
        level4.start(stage);
    }
}