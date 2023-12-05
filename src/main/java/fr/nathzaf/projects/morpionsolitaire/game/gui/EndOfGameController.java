package fr.nathzaf.projects.morpionsolitaire.game.gui;

import fr.nathzaf.projects.morpionsolitaire.game.GameManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class EndOfGameController {

    @FXML
    private Text playerScoreText;

    public void displayEndOfGame(GameManager gameManager) {
        playerScoreText.setText("Your score is: " + gameManager.getBoard().getScore());
    }

    public void newGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChoosePlayerNameAndMode.fxml"));
        Parent root = loader.load();

        ChoosePlayerNameAndModeController choosePlayerNameAndModeController = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void quit(ActionEvent event) {
        Platform.exit();
    }
}
