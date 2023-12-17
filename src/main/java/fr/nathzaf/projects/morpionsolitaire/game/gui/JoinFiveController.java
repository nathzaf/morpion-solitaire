package fr.nathzaf.projects.morpionsolitaire.game.gui;

import fr.nathzaf.projects.morpionsolitaire.components.Alignment;
import fr.nathzaf.projects.morpionsolitaire.components.Board;
import fr.nathzaf.projects.morpionsolitaire.components.Point;
import fr.nathzaf.projects.morpionsolitaire.solver.RandomSolver;
import fr.nathzaf.projects.morpionsolitaire.solver.Solver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinFiveController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinFiveController.class);

    @FXML
    private Pane joinFivePane;

    @FXML
    private Text playerScoreText;

    @FXML
    private Text playerNameText;

    @FXML
    private Text gameModeText;

    @FXML
    private Button surrenderButton;

    @FXML
    private Button hintButton;

    @FXML
    private Button undoButton;

    @FXML
    private Button randomSolverButton;

    private Board board;

    private Scene scene;

    private boolean hint = false;

    private boolean multipleAlignments = false;

    private boolean autoSolved = false;

    private Set<Point> multipleAlignmentsCandidates = new HashSet<>();

    private Set<Point> hintPoints = new HashSet<>();

    private MediaPlayer mediaPlayer;

    public void selectPoint(MouseEvent event) throws IOException {
        if(!multipleAlignments) {
            Circle circle = (Circle) event.getSource();

            Point selectedPoint = convertPointIdToPoint(circle.getId());
            if (hint) {
                hint = false;
                hintPoints.remove(selectedPoint);
                for (Point point : hintPoints)
                    setCircleOpacityFromPoint(point, 0);
            }

            if (board.getPoints().contains(selectedPoint) && circle.getOpacity() == 0
                    || !board.getPoints().contains(selectedPoint) && circle.getOpacity() == 1)
                throw new IllegalStateException("Illegal state of the board.");

            if (!board.getPoints().contains(selectedPoint)
                    && board.getPossibleMoves().contains(selectedPoint)) {
                List<Alignment> possibleAlignments = board.addPoint(selectedPoint).stream().toList();
                if (possibleAlignments.size() == 1) {
                    board.addAlignment(possibleAlignments.get(0));
                    circle.setOpacity(1);
                    drawLine(possibleAlignments.get(0));
                } else if (possibleAlignments.size() > 1) {
                    if(!autoSolved)
                        MusicPlayer.playSoundEffectFromGUIPackage("choice_sound.mp3");
                    hintButton.setDisable(true);
                    randomSolverButton.setDisable(true);
                    undoButton.setDisable(true);
                    multipleAlignments = true;
                    Map<Point, Alignment> possibleAlignmentsMap = new HashMap<>();
                    multipleAlignmentsCandidates = possibleAlignmentsMap.keySet();
                    LOGGER.info("Multiple alignment possible for this point :");
                    for (Alignment alignment : possibleAlignments){
                        LOGGER.info("{}", alignment);
                        possibleAlignmentsMap.put(alignment.getExtremities().get(0), alignment);
                    }
                    if(!multipleAlignmentsCandidates.contains(selectedPoint))
                        circle.setOpacity(1);
                    for (Point point : possibleAlignmentsMap.keySet()) {
                        setCircleOpacityFromPoint(point, 1);
                        setCircleColorFromPoint(point, Color.GREEN);
                        setCircleCreatingAlignmentFormPoint(point, possibleAlignmentsMap.get(point));
                    }
                }
                addNumberOnPoint(circle, board.getScore());
                updatePlayerScoreText();
                if (board.getPossibleMoves().isEmpty()) {
                    endOfGame();
                }
            }
        }
    }

    private void setCircleCreatingAlignmentFormPoint(Point point, Alignment alignment) {
        Circle circle = (Circle) scene.lookup(point.generateCircleId());
        if(circle == null)
            throw new IllegalArgumentException("This point is not associated to a circle.");
        circle.setOnMouseClicked((MouseEvent e) -> {
            hintButton.setDisable(false);
            randomSolverButton.setDisable(false);
            undoButton.setDisable(false);
            board.addAlignment(alignment);
            circle.setFill(Color.web("#f81010"));
            drawLine(alignment);
            multipleAlignmentsCandidates.remove(point);
            multipleAlignments = false;
            for(Point candidate : multipleAlignmentsCandidates) {
                if(board.getPoints().contains(candidate))
                    setCircleOpacityFromPoint(candidate, 1);
                else
                    setCircleOpacityFromPoint(candidate, 0);
                setCircleColorFromPoint(candidate, Color.web("#f81010"));
            }
            multipleAlignmentsCandidates.clear();
        });
    }

    public void reset(ActionEvent event) throws IOException {
        Board board = new Board(this.board.getGameMode(),
                this.board.getPlayerName());

        LOGGER.info("Reseting the game.");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("JoinFive.fxml"));
        Parent root = loader.load();

        JoinFiveController joinFiveController = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        joinFiveController.initializeGame(scene, board);
        stage.show();
    }

    public void randomSolver(ActionEvent event) {
        LOGGER.info("Using random solver.");
        autoSolved = true;
        Solver randomSolver = new RandomSolver(board);
        randomSolver.solve();
        updateBoard();
        MusicPlayer.playSoundEffectFromGUIPackage("auto_solver_sound.mp3");
        LOGGER.info("Random solver done.");
        surrenderButton.setText("End screen");
        undoButton.setDisable(true);
        hintButton.setDisable(true);
    }

    public void surrender(ActionEvent event) throws IOException {
        LOGGER.info("Surrending the game.");
        endOfGame();
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

    public void undo(ActionEvent event) {
        if(board.getScore() > 0) {
            Point undoPoint = board.undo();
            setCircleOpacityFromPoint(undoPoint, 0);
            updatePlayerScoreText();
            Line undoLine = (Line) scene.lookup("#line" + (board.getScore() + 1));
            joinFivePane.getChildren().remove(undoLine);
            Text undoPointScoreText = (Text) scene.lookup("#pointNumber" + (board.getScore() + 1));
            joinFivePane.getChildren().remove(undoPointScoreText);
        }
    }

    public void hint(ActionEvent event) {
        MusicPlayer.playSoundEffectFromGUIPackage("hint_sound.mp3");
        LOGGER.info("Use of hint button.");
        hint = true;
        hintPoints.clear();
        hintPoints = board.getPossibleMoves();
        for(Point point : hintPoints)
            setCircleOpacityFromPoint(point, 0.5);
    }

    private void setCircleOpacityFromPoint(Point point, double opacity) {
        Circle circle = (Circle) scene.lookup(point.generateCircleId());
        if(circle == null)
            throw new IllegalArgumentException("This point is not associated to a circle.");
        circle.setOpacity(opacity);
    }

    private void setCircleColorFromPoint(Point point, Color color) {
        Circle circle = (Circle) scene.lookup(point.generateCircleId());
        if(circle == null)
            throw new IllegalArgumentException("This point is not associated to a circle.");
        circle.setFill(color);
    }

    private void addNumberOnPoint(Circle circle, int number) {
        if(number != -1) { //case of initialized points
            if((number == 1 || number % 5 == 0) && !autoSolved)
                MusicPlayer.playSoundEffectFromGUIPackage("score_sound.mp3");
            Text numberText = new Text(String.valueOf(number));
            int dx = number < 10 ? 10 : 15;
            Font font = new Font("Chakra Petch Regular", 12);
            numberText.setFont(font);
            numberText.setX(circle.getCenterX() - dx);
            numberText.setY(circle.getCenterY() + 15);
            numberText.setStroke(Color.YELLOW);
            numberText.setFill(Color.YELLOW);
            numberText.setId("pointNumber" + number);
            joinFivePane.getChildren().add(numberText);
        }
    }

    private void endOfGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EndOfGame.fxml"));
        Parent root = loader.load();

        EndOfGameController endOfGameController = loader.getController();
        endOfGameController.displayEndOfGame(board, autoSolved);
        if(mediaPlayer != null)
            mediaPlayer.stop();

        Stage stage = (Stage) joinFivePane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void drawLine(Alignment alignment) {
        List<Point> extremities = alignment.getExtremities();
        Line line = new Line();

        Point startPoint = extremities.get(0);
        Circle startCircle = (Circle) scene.lookup(startPoint.generateCircleId());

        Point endPoint = extremities.get(1);
        Circle endCircle = (Circle) scene.lookup(endPoint.generateCircleId());

        if(startCircle == null || endCircle == null) {
            throw new NullPointerException("Can't found associated circles.");
        }

        line.setStartX(startCircle.getCenterX());
        line.setStartY(startCircle.getCenterY());
        line.setEndX(endCircle.getCenterX());
        line.setEndY(endCircle.getCenterY());
        line.setStrokeWidth(2);
        line.setStroke(Color.YELLOW);
        line.setId("line"+ board.getScore());

        joinFivePane.getChildren().add(line);
    }

    public void initializeGame(Scene scene, Board board) {
        this.board = board;
        this.scene = scene;
        playerScoreText.setText("0");
        playerNameText.setText(board.getPlayerName());
        gameModeText.setText(board.getGameMode().getId());
        if(mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = MusicPlayer.playMusicFromGUIPackage("join_five_music.mp3");
        board.initialize();
        for (Point point : board.getPoints()) {
            Circle circle = (Circle) scene.lookup(point.generateCircleId());
            circle.setOpacity(1);
        }
    }

    private void updateBoard() {
        for(Point point : board.getPoints()) {
            Circle circle = (Circle) scene.lookup(point.generateCircleId());
            if(circle == null)
                throw new NullPointerException();
            circle.setOpacity(1);
            addNumberOnPoint(circle, point.getScore());
        }
        for(Alignment alignment : board.getAlignments()) {
            drawLine(alignment);
        }
        updatePlayerScoreText();
    }

    private Point convertPointIdToPoint(String id) {
        Pattern pattern = Pattern.compile("x(\\d{1,2})y(\\d{1,2})");
        Matcher matcher = pattern.matcher(id);

        if(matcher.find()) {
            return new Point(Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)));
        }
        throw new IllegalArgumentException("Can't convert the given id.");
    }

    private void updatePlayerScoreText() {
        playerScoreText.setText(String.valueOf(board.getScore()));
    }
}
