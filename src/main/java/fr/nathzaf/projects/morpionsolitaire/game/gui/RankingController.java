package fr.nathzaf.projects.morpionsolitaire.game.gui;

import fr.nathzaf.projects.morpionsolitaire.game.Mode;
import fr.nathzaf.projects.morpionsolitaire.ranking.GameHistory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class RankingController {

    private static final int RANKING_SIZE = 10;

    @FXML
    HBox rankingHBox;

    private static final Logger LOGGER = LoggerFactory.getLogger(RankingController.class);

    private MediaPlayer mediaPlayer;

    public void displayRanking() {
        LOGGER.info("Displaying ranking");
        if(mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = MusicPlayer.playMusicFromGUIPackage("ranking_music.mp3");
        VBox rankingNormalVBox = new VBox();
        rankingNormalVBox.setSpacing(40);
        VBox rankingSharpVBox = new VBox();
        rankingSharpVBox.setSpacing(40);
        rankingHBox.getChildren().add(rankingNormalVBox);
        rankingHBox.getChildren().add(rankingSharpVBox);
        for(Mode mode : Mode.values()) {
            VBox vbox = new VBox(new Text(mode.getId()));
            vbox.setAlignment(Pos.CENTER);
            List<GameHistory> gameHistory = GameHistory.getAllGameHistoryByMode(mode);
            if (!gameHistory.isEmpty()) {
                if (gameHistory.size() > RANKING_SIZE) {
                    gameHistory = gameHistory.subList(0, RANKING_SIZE);
                }
                gameHistory.sort(Comparator.comparing(GameHistory::getScore).reversed());
                for (int i = 0; i < gameHistory.size(); i++) {
                    Text rankingLine = new Text("Number " + (i + 1) + " : " + gameHistory.get(i).getPlayerName() +
                            " with a score of " + gameHistory.get(i).getScore() + " " + gameHistory.get(i).getAutoSolver());
                    vbox.getChildren().add(rankingLine);
                }
            } else {
                vbox.getChildren().add(new Text("No games to show."));
            }
            if(mode.isSharpMode()) {
                rankingSharpVBox.getChildren().add(vbox);
            } else {
                rankingNormalVBox.getChildren().add(vbox);
            }
        }
    }

    public void returnToMainTitle(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainTitle.fxml"));
        Parent root = loader.load();

        MainTitleController mainTitleController = loader.getController();
        if(mediaPlayer != null)
            mediaPlayer.stop();
        mainTitleController.init();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void quit(ActionEvent event){
        LOGGER.info("Quitting the game.");
        Platform.exit();
    }
}
