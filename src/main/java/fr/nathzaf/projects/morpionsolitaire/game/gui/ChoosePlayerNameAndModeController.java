package fr.nathzaf.projects.morpionsolitaire.game.gui;

import fr.nathzaf.projects.morpionsolitaire.components.Board;
import fr.nathzaf.projects.morpionsolitaire.game.Mode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ChoosePlayerNameAndModeController {

    @FXML
    private TextField playerNameTextField;

    @FXML
    private RadioButton mode5TRadioButton;

    @FXML
    private RadioButton mode5DRadioButton;

    private Mode mode = null;

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

        if (valid) {
            Board board = new Board(mode, playerName);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("JoinFive.fxml"));
            Parent root = loader.load();

            JoinFiveController joinFiveController = loader.getController();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            joinFiveController.initializeGame(scene, board);
            stage.show();
        }
    }

    public void getMode(ActionEvent event) {
        if(mode5DRadioButton.isSelected())
            mode = Mode.DISJOINT;
        else if(mode5TRadioButton.isSelected())
            mode = Mode.TOUCHING;
    }
}
