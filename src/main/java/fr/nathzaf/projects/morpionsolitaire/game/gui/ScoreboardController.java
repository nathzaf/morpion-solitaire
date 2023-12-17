package fr.nathzaf.projects.morpionsolitaire.game.gui;

import fr.nathzaf.projects.morpionsolitaire.game.Mode;
import fr.nathzaf.projects.morpionsolitaire.ranking.GameHistory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ScoreboardController {

    private static final int RANKING_SIZE = 10;

    @FXML
    VBox ranking5TVBox;

    @FXML
    VBox ranking5DVBox;

    @FXML
    VBox ranking5TSharpVBox;

    @FXML
    VBox ranking5DSharpVBox;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreboardController.class);

    private MediaPlayer mediaPlayer;

    public void displayRanking() {
        LOGGER.info("Displaying ranking");
        if(mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = MusicPlayer.playMusicFromGUIPackage("ranking_music.mp3");
        fillRankingVBox(ranking5TVBox, Mode.TOUCHING);
        fillRankingVBox(ranking5DVBox, Mode.DISJOINT);
        fillRankingVBox(ranking5TSharpVBox, Mode.TOUCHING_SHARP);
        fillRankingVBox(ranking5DSharpVBox, Mode.DISJOINT_SHARP);
    }

    private void fillRankingVBox(VBox vBox, Mode mode) {
        List<GameHistory> gameHistory = GameHistory.getAllGameHistoryByMode(mode);
        if (!gameHistory.isEmpty()) {
            gameHistory.sort(Comparator.comparing(GameHistory::getScore).reversed());
            if (gameHistory.size() > RANKING_SIZE) {
                gameHistory = gameHistory.subList(0, RANKING_SIZE);
            }
            Text modeText = new Text(mode.getId());
            modeText.setFill(Color.WHITE);
            modeText.setLineSpacing(1);
            modeText.setUnderline(true);
            Font fontMode = new Font("Chakra Petch Regular", 15);
            modeText.setFont(fontMode);
            vBox.getChildren().add(modeText);
            vBox.getChildren().add(new Text());
            for (int i = 0; i < gameHistory.size(); i++) {
                Text rankingLine = new Text("#" + (i + 1) + " : " + gameHistory.get(i).getPlayerName() +
                        " with a score of " + gameHistory.get(i).getScore() + " " + gameHistory.get(i).getAutoSolver());
                rankingLine.setFill(Color.WHITE);
                rankingLine.setLineSpacing(1);
                Font font = new Font( "Chakra Petch Regular", 15);
                rankingLine.setFont(font);
                vBox.getChildren().add(rankingLine);
            }
        } else {
            vBox.getChildren().add(new Text("No games to show."));
        }
    }

    public void returnToMainTitle(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainTitle.fxml"));
        Parent root = loader.load();

        MainTitleController mainTitleController = loader.getController();
        if(mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = MusicPlayer.playMusicFromGUIPackage("main_title_music.mp3");

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
