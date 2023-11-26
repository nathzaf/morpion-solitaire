package fr.nathzaf.projects.morpionsolitaire.game.gui;

import fr.nathzaf.projects.morpionsolitaire.components.Alignment;
import fr.nathzaf.projects.morpionsolitaire.components.Point;
import fr.nathzaf.projects.morpionsolitaire.game.GameManagerFx;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinFiveController {

    @FXML
    private Pane joinFivePane;

    private GameManagerFx gameManager;

    private Scene scene;

    public void selectPoint(MouseEvent event) throws IOException {
        Circle circle = (Circle) event.getSource();

        Point selectedPoint = convertPointIdToPoint(circle.getId());

        if(gameManager.getBoard().getPoints().contains(selectedPoint) && circle.getOpacity() == 0
        || !gameManager.getBoard().getPoints().contains(selectedPoint) && circle.getOpacity() == 1)
                throw new IllegalStateException("Illegal state of the board.");

        if(!gameManager.getBoard().getPoints().contains(selectedPoint)
                && gameManager.getBoard().getPossibleMoves().contains(selectedPoint)) {
            List<Alignment> possibleAlignments = gameManager.getBoard().addPoint(selectedPoint).stream().toList();
            if(possibleAlignments.size() == 1) {
                gameManager.getBoard().addAlignment(possibleAlignments.get(0));
                circle.setOpacity(1);
                drawLine(possibleAlignments.get(0));

                if(gameManager.getBoard().getPossibleMoves().isEmpty()){
                    endOfGame();
                }
                //TODO HANDLE MULTIPLE ALIGNMENTS
            }
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

        joinFivePane.getChildren().add(line);
    }

    public void initializeGame(Scene scene, GameManagerFx gameManager) {
        this.gameManager = gameManager;
        this.scene = scene;
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

    private Point convertPointIdToPoint(String id) {
        Pattern pattern = Pattern.compile("x(\\d{1,2})y(\\d{1,2})");
        Matcher matcher = pattern.matcher(id);

        if(matcher.find()) {
            return new Point(Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)));
        }
        throw new IllegalArgumentException("Can't convert the given id.");
    }
}
