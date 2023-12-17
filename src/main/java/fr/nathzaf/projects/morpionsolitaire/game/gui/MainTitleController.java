package fr.nathzaf.projects.morpionsolitaire.game.gui;

import fr.nathzaf.projects.morpionsolitaire.components.Board;
import fr.nathzaf.projects.morpionsolitaire.game.Mode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;

public class MainTitleController {

    @FXML
    private TextField playerNameTextField;

    @FXML
    private RadioButton mode5TRadioButton;

    @FXML
    private RadioButton mode5DRadioButton;

    @FXML
    private CheckBox sharpModeCheckBox;

    private Mode mode = null;

    private MediaPlayer mediaPlayer;

    /**
     * Init the main title screen by starting the associated music.
     */
    public void init() {
        mediaPlayer = MusicPlayer.playMusicFromGUIPackage("main_title_music.mp3");
    }

    /**
     * Handle clicking "Launch game!" button. Starts the game with the specified player name and mode.
     * Open an alert if an information is missing.
     *
     * @param event
     * @throws IOException
     */
    public void startGame(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error has occured");
        alert.setHeaderText("Error");
        boolean valid = true;

        String playerName = playerNameTextField.getText();

        if(playerName.isBlank()) {
            valid = false;
            alert.setHeaderText("Player name can't be blank !");
            alert.show();
        }
        else if(mode == null) {
            valid = false;
            alert.setHeaderText("A mode must be selected !");
            alert.show();
        }

        if (sharpModeCheckBox.isSelected()) {
            if (mode == Mode.TOUCHING)
                mode = Mode.TOUCHING_SHARP;
            else if(mode == Mode.DISJOINT)
                mode = Mode.DISJOINT_SHARP;
        }

        if (valid) {
            Board board = new Board(mode, playerName);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("JoinFive.fxml"));
            Parent root = loader.load();

            if(mediaPlayer != null)
                mediaPlayer.stop();

            JoinFiveController joinFiveController = loader.getController();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            joinFiveController.initializeGame(scene, board);
            stage.show();
        }
    }

    /**
     * Handle clicking "Ranking" button, send the user to the scoreboard screen.
     *
     * @param event
     * @throws IOException
     */
    public void displayRanking(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Scoreboard.fxml"));
        Parent root = loader.load();

        if (mediaPlayer != null)
            mediaPlayer.stop();

        ScoreboardController scoreboardController = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scoreboardController.displayRanking();
        stage.show();
    }

    /**
     * Get the mode selected in radio buttons.
     *
     * @param event
     */
    public void getMode(ActionEvent event) {
        if(mode5DRadioButton.isSelected())
            mode = Mode.DISJOINT;
        else if(mode5TRadioButton.isSelected())
            mode = Mode.TOUCHING;
    }

    /**
     * Handle clicking "Quit" button, close the window.
     *
     * @param event
     */
    public void quit(ActionEvent event){
        Platform.exit();
    }
}
