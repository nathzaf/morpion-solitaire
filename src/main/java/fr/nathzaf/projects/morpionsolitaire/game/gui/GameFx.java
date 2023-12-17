package fr.nathzaf.projects.morpionsolitaire.game.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Objects;

public class GameFx extends Application {

    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("zelda_font.otf"), 0);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainTitle.fxml")));
        Image gameIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png")));
        if(Taskbar.isTaskbarSupported()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            if(taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                java.awt.Image toolkitImage = defaultToolkit.getImage(getClass().getResource("logo.png"));
                taskbar.setIconImage(toolkitImage);
            }
        }
        if(mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = MusicPlayer.playMusicFromGUIPackage("main_title_music.mp3");
        stage.getIcons().add(gameIcon);
        stage.setTitle("Morpion Solitaire");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

}
