package fr.nathzaf.projects.morpionsolitaire.game.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class GameFx extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ChoosePlayerNameAndMode.fxml")));
        stage.setTitle("Morpion Solitaire");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
