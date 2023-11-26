package fr.nathzaf.projects.morpionsolitaire.game.gui;

import fr.nathzaf.projects.morpionsolitaire.components.Alignment;
import fr.nathzaf.projects.morpionsolitaire.components.Point;
import fr.nathzaf.projects.morpionsolitaire.game.GameManager;
import fr.nathzaf.projects.morpionsolitaire.solver.RandomSolver;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinFiveController {

    @FXML
    private Pane joinFivePane;

    @FXML
    private Text playerScoreText;

    @FXML
    private Text playerName;

    @FXML
    private Button surrenderButton;

    @FXML
    private Button hintButton;

    @FXML
    private Button undoButton;

    @FXML
    private Button randomSolverButton;

    private GameManager gameManager;

    private Scene scene;

    private boolean hint = false;

    private boolean multipleAlignments = false;

    private Set<Point> multipleAlignmentsCandidates = new HashSet<>();

    private Set<Point> hintPoints = new HashSet<>();

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

            if (gameManager.getBoard().getPoints().contains(selectedPoint) && circle.getOpacity() == 0
                    || !gameManager.getBoard().getPoints().contains(selectedPoint) && circle.getOpacity() == 1)
                throw new IllegalStateException("Illegal state of the board.");

            if (!gameManager.getBoard().getPoints().contains(selectedPoint)
                    && gameManager.getBoard().getPossibleMoves().contains(selectedPoint)) {
                List<Alignment> possibleAlignments = gameManager.getBoard().addPoint(selectedPoint).stream().toList();
                if (possibleAlignments.size() == 1) {
                    gameManager.getBoard().addAlignment(possibleAlignments.get(0));
                    circle.setOpacity(1);
                    drawLine(possibleAlignments.get(0));
                } else if (possibleAlignments.size() > 1) {
                    hintButton.setDisable(true);
                    randomSolverButton.setDisable(true);
                    undoButton.setDisable(true);
                    multipleAlignments = true;
                    Map<Point, Alignment> possibleAlignmentsMap = new HashMap<>();
                    multipleAlignmentsCandidates = possibleAlignmentsMap.keySet();
                    for (Alignment alignment : possibleAlignments)
                        possibleAlignmentsMap.put(alignment.getExtremities().get(0), alignment);
                    if(!multipleAlignmentsCandidates.contains(selectedPoint))
                        circle.setOpacity(1);
                    for (Point point : possibleAlignmentsMap.keySet()) {
                        setCircleOpacityFromPoint(point, 1);
                        setCircleColorFromPoint(point, Color.GREEN);
                        setCircleCreatingAlignmentFormPoint(point, possibleAlignmentsMap.get(point));
                    }
                }
                addNumberOnPoint(circle, gameManager.getBoard().getScore());
                updatePlayerScoreText();
                if (gameManager.getBoard().getPossibleMoves().isEmpty())
                    endOfGame();
            }
        }
    }

    private void setCircleCreatingAlignmentFormPoint(Point point, Alignment alignment) {
        Circle circle = (Circle) scene.lookup("#x" + point.getX() + "y" + point.getY());
        if(circle == null)
            throw new IllegalArgumentException("This point is not associated to a circle.");
        circle.setOnMouseClicked((MouseEvent e) -> {
            hintButton.setDisable(false);
            randomSolverButton.setDisable(false);
            undoButton.setDisable(false);
            gameManager.getBoard().addAlignment(alignment);
            circle.setFill(Color.BLACK);
            drawLine(alignment);
            multipleAlignmentsCandidates.remove(point);
            multipleAlignments = false;
            for(Point candidate : multipleAlignmentsCandidates) {
                if(gameManager.getBoard().getPoints().contains(candidate))
                    setCircleOpacityFromPoint(candidate, 1);
                else
                    setCircleOpacityFromPoint(candidate, 0);
                setCircleColorFromPoint(candidate, Color.BLACK);
            }
            multipleAlignmentsCandidates.clear();
        });
    }

    public void reset(ActionEvent event) throws IOException {
        GameManager gameManager = new GameManager(this.gameManager.getBoard().getGameMode(),
                this.gameManager.getPlayerName());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("JoinFive.fxml"));
        Parent root = loader.load();

        JoinFiveController joinFiveController = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        joinFiveController.initializeGame(scene, gameManager);
        stage.show();
    }

    public void randomSolver(ActionEvent event) {
        RandomSolver randomSolver = new RandomSolver(gameManager.getBoard());
        randomSolver.solve();
        updateBoard();
        surrenderButton.setText("Go to end screen");
        undoButton.setDisable(true);
        hintButton.setDisable(true);
    }

    public void surrender(ActionEvent event) throws IOException {
        endOfGame();
    }

    public void returnToMainTitle(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChoosePlayerNameAndMode.fxml"));
        Parent root = loader.load();

        ChoosePlayerNameAndModeController choosePlayerNameAndModeController = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void quit(ActionEvent event){
        Platform.exit();
    }

    public void undo(ActionEvent event) {
        if(gameManager.getBoard().getScore() > 0) {
            Point undoPoint = gameManager.getBoard().undo();
            setCircleOpacityFromPoint(undoPoint, 0);
            updatePlayerScoreText();
            Line undoLine = (Line) scene.lookup("#line" + (gameManager.getBoard().getScore() + 1));
            joinFivePane.getChildren().remove(undoLine);
            Text undoText = (Text) scene.lookup("#pointNumber" + (gameManager.getBoard().getScore() + 1));
            joinFivePane.getChildren().remove(undoText);
        }
    }

    public void hint(ActionEvent event) {
        hint = true;
        hintPoints.clear();
        hintPoints = gameManager.getBoard().getPossibleMoves();
        for(Point point : hintPoints)
            setCircleOpacityFromPoint(point, 0.5);
    }

    private void setCircleOpacityFromPoint(Point point, double opacity) {
        Circle circle = (Circle) scene.lookup("#x" + point.getX() + "y" + point.getY());
        if(circle == null)
            throw new IllegalArgumentException("This point is not associated to a circle.");
        circle.setOpacity(opacity);
    }

    private void setCircleColorFromPoint(Point point, Color color) {
        Circle circle = (Circle) scene.lookup("#x" + point.getX() + "y" + point.getY());
        if(circle == null)
            throw new IllegalArgumentException("This point is not associated to a circle.");
        circle.setFill(color);
    }

    private void addNumberOnPoint(Circle circle, int number) {
        if(number != -1) { //case of initialized points
            Text numberText = new Text(String.valueOf(number));
            int dx = number < 10 ? 10 : 15;
            numberText.setX(circle.getCenterX() - dx);
            numberText.setY(circle.getCenterY() + 15);
            numberText.setStroke(Color.RED);
            numberText.setId("pointNumber" + number);
            joinFivePane.getChildren().add(numberText);
        }
    }

    private void endOfGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EndOfGame.fxml"));
        Parent root = loader.load();

        EndOfGameController endOfGameController = loader.getController();
        endOfGameController.displayEndOfGame(gameManager);

        Stage stage = (Stage) joinFivePane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void drawLine(Alignment alignment) {
        List<Point> extremities = alignment.getExtremities();
        Line line = new Line();

        Point startPoint = extremities.get(0);
        Circle startCircle = (Circle) scene.lookup("#x"+startPoint.getX()+"y"+startPoint.getY());

        Point endPoint = extremities.get(1);
        Circle endCircle = (Circle) scene.lookup("#x"+endPoint.getX()+"y"+endPoint.getY());

        if(startCircle == null || endCircle == null) {
            throw new NullPointerException("Can't found associated circles.");
        }

        line.setStartX(startCircle.getCenterX());
        line.setStartY(startCircle.getCenterY());
        line.setEndX(endCircle.getCenterX());
        line.setEndY(endCircle.getCenterY());
        line.setStrokeWidth(2);
        line.setStroke(Color.BLUE);
        line.setId("line"+gameManager.getBoard().getScore());

        joinFivePane.getChildren().add(line);
    }

    public void initializeGame(Scene scene, GameManager gameManager) {
        this.gameManager = gameManager;
        this.scene = scene;
        playerScoreText.setText("0");
        playerName.setText(gameManager.getPlayerName());
        final int x = 6;
        final int y = 3;
        final int width = 4;

        for (int i = 0; i < width; i++) {
            Circle circle1 = (Circle) scene.lookup("#x" + (x + i) + "y" + y);
            circle1.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x + i, y));

            Circle circle2 = (Circle) scene.lookup("#x" + (x + i - width + 1) + "y" + (y + width - 1));
            circle2.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x + i - width + 1, y + width - 1));

            Circle circle3 = (Circle) scene.lookup("#x" + (x + i + width - 1) + "y" + (y + width - 1));
            circle3.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x + i + width - 1, y + width - 1));

            Circle circle4 = (Circle) scene.lookup("#x" + (x + i - width + 1) + "y" + (y + 2 * width - 2));
            circle4.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x + i - width + 1, y + 2 * width - 2));

            Circle circle5 = (Circle) scene.lookup("#x" + (x + i + width - 1) + "y" + (y + 2 * width - 2));
            circle5.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x + i + width - 1, y + 2 * width - 2));

            Circle circle6 = (Circle) scene.lookup("#x" + (x + i) + "y" + (y + 3 * width - 3));
            circle6.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x + i, y + 3 * width - 3));

            Circle circle7 = (Circle) scene.lookup("#x" + x + "y" + (y + i));
            circle7.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x, y + i));

            Circle circle8 = (Circle) scene.lookup("#x" + (x + width - 1) + "y" + (y + i));
            circle8.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x + width - 1, y + i));

            Circle circle9 = (Circle) scene.lookup("#x" + (x - width + 1) + "y" + (y + i + width - 1));
            circle9.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x - width + 1, y + i + width - 1));

            Circle circle10 = (Circle) scene.lookup("#x" + (x + 2 * width - 2) + "y" + (y + i + width - 1));
            circle10.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x + 2 * width - 2, y + i + width - 1));

            Circle circle11 = (Circle) scene.lookup("#x" + x + "y" + (y + i + 2 * width - 2));
            circle11.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x, y + i + 2 * width - 2));

            Circle circle12 = (Circle) scene.lookup("#x" + (x + width - 1) + "y" + (y + i + 2 * width - 2));
            circle12.setOpacity(1);
            gameManager.getBoard().getPoints().add(new Point(x + width - 1, y + i + 2 * width - 2));
        }
    }

    private void updateBoard() {
        for(Point point : gameManager.getBoard().getPoints()) {
            Circle circle = (Circle) scene.lookup("#x" + point.getX() + "y" + point.getY());
            if(circle == null)
                throw new NullPointerException();
            circle.setOpacity(1);
            addNumberOnPoint(circle, point.getScore());
        }
        for(Alignment alignment : gameManager.getBoard().getAlignments()) {
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
        playerScoreText.setText(String.valueOf(gameManager.getBoard().getScore()));
    }
}
