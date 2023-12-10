package fr.nathzaf.projects.morpionsolitaire.game.gui;

import fr.nathzaf.projects.morpionsolitaire.components.Board;
import fr.nathzaf.projects.morpionsolitaire.ranking.GameHistory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EndOfGameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndOfGameController.class);

    @FXML
    private Text playerScoreText;

    public void displayEndOfGame(Board board, boolean autoSolved) {
        String autoSolver = autoSolved ? "(auto solved randomly)" : "";
        playerScoreText.setText("Your score is: " + board.getScore() + " " + autoSolver + "\nIt has been registered on database.");
        GameHistory.addNewGameHistory(board.getPlayerName(), board.getGameMode().getId(), autoSolver, board.getScore());
        LOGGER.info("Game has ended with a score of {}.", board.getScore());
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

    public void displayRanking(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Ranking.fxml"));
        Parent root = loader.load();

        RankingController rankingController = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        rankingController.displayRanking();
        stage.show();
    }

    public void quit(ActionEvent event) {
        Platform.exit();
    }
}
