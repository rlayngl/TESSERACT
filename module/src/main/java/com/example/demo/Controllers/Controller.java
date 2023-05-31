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
    @FXML
    protected void PlayLevel0() {
        Level0Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        Level0.start(stage);
    }
    @FXML
    protected void PlayLevel1() {
        Level1Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        Level1.start(stage);
    }
    @FXML
    protected void PlayLevel2() {
        Level1Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        Level2.start(stage);
    }
    @FXML
    protected void PlayLevel3() {
        Level1Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        Level3.start(stage);
    }
    @FXML
    protected void PlayLevel4() {
        Level4Button.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setTitle("TESSERACT");
        stage.getIcons().add(new Image("Images/Icon.png"));
        stage.setFullScreen(true);
        stage.show();
        Level4.start(stage);
    }
}