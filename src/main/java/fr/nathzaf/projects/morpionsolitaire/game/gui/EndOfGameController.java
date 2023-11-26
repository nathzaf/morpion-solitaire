package fr.nathzaf.projects.morpionsolitaire.game.gui;

import fr.nathzaf.projects.morpionsolitaire.game.GameManagerFx;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class EndOfGameController {

    @FXML
    private Text playerScoreText;

    public void displayEndOfGame(GameManagerFx gameManager) {
        playerScoreText.setText(String.valueOf(gameManager.getBoard().getScore()));
    }
}
