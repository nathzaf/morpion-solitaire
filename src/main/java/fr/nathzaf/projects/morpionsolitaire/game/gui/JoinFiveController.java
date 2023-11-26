package fr.nathzaf.projects.morpionsolitaire.game.gui;

import fr.nathzaf.projects.morpionsolitaire.game.GameManagerFx;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class JoinFiveController {

    private GameManagerFx gameManager;

    public void launchGame(GameManagerFx gameManager) {
        this.gameManager = gameManager;
    }

    public void selectPoint(MouseEvent event) {
        Circle circle = (Circle) event.getSource();
        System.out.println("Point cliqué : " + circle.getId());
        circle.setOpacity(1);
    }

    public void initializeBoard(Scene scene) {
        final int x = 6;
        final int y = 3;
        final int width = 4;

        for (int i = 0; i < width; ++i) {
            Circle circle1 = (Circle) scene.lookup("#x" + (x + i) + "y" + y);
            circle1.setOpacity(1);

            Circle circle2 = (Circle) scene.lookup("#x" + (x + i - width + 1) + "y" + (y + width - 1));
            circle2.setOpacity(1);

            Circle circle3 = (Circle) scene.lookup("#x" + (x + i + width - 1) + "y" + (y + width - 1));
            circle3.setOpacity(1);

            Circle circle4 = (Circle) scene.lookup("#x" + (x + i - width + 1) + "y" + (y + 2 * width - 2));
            circle4.setOpacity(1);

            Circle circle5 = (Circle) scene.lookup("#x" + (x + i + width - 1) + "y" + (y + 2 * width - 2));
            circle5.setOpacity(1);

            Circle circle6 = (Circle) scene.lookup("#x" + (x + i) + "y" + (y + 3 * width - 3));
            circle6.setOpacity(1);

            Circle circle7 = (Circle) scene.lookup("#x" + x + "y" + (y + i));
            circle7.setOpacity(1);

            Circle circle8 = (Circle) scene.lookup("#x" + (x + width - 1) + "y" + (y + i));
            circle8.setOpacity(1);

            Circle circle9 = (Circle) scene.lookup("#x" + (x - width + 1) + "y" + (y + i + width - 1));
            circle9.setOpacity(1);

            Circle circle10 = (Circle) scene.lookup("#x" + (x + 2 * width - 2) + "y" + (y + i + width - 1));
            circle10.setOpacity(1);

            Circle circle11 = (Circle) scene.lookup("#x" + x + "y" + (y + i + 2 * width - 2));
            circle11.setOpacity(1);

            Circle circle12 = (Circle) scene.lookup("#x" + (x + width - 1) + "y" + (y + i + 2 * width - 2));
            circle12.setOpacity(1);
        }
    }
}
