package fr.nathzaf.projects.morpionsolitaire.game;

import fr.nathzaf.projects.morpionsolitaire.components.Alignment;
import fr.nathzaf.projects.morpionsolitaire.components.BoardFx;
import fr.nathzaf.projects.morpionsolitaire.components.Point;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GameManagerFx {

    private final BoardFx board;

    private final Scanner scanner;

    private final String playerName;

    /**
     * Initializes a new game with the specified mode.
     *
     * @param mode The game mode, either TOUCHING or DISJOINT.
     */
    public GameManagerFx(Mode mode, String playerName) {
        this.board = new BoardFx(mode);
        this.scanner = new Scanner(System.in);
        this.playerName = playerName;
    }




    /**
     * Handles a single turn in the game, prompting the user for input and making the move.
     */
    private void playTurn() {
        int x, y;

        System.out.print("Enter the X coordinate of the point you want to place: ");
        while (true) {
            try {
                x = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer.");
                scanner.next();
            }
        }

        System.out.print("Enter the Y coordinate of the point you want to place: ");
        while (true) {
            try {
                y = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer.");
                scanner.next();
            }
        }

        Point point = new Point(x, y);
        List<Alignment> possibleAlignments = board.addPoint(point).stream().toList();
        if (possibleAlignments.isEmpty()) {
            System.out.println("Invalid move! Try again.");
        } else if (possibleAlignments.size() == 1) {
            System.out.println("Point added successfully!");
            board.addAlignment(possibleAlignments.get(0));
        } else {
            System.out.println("Select the alignment desired.");
            for (int i = 0; i < possibleAlignments.size(); i++)
                System.out.println(i + 1 + ": " + possibleAlignments.get(i));
            int desiredAlignments = 0;
            while (desiredAlignments < 1 || desiredAlignments > possibleAlignments.size()) {
                while (true) {
                    try {
                        desiredAlignments = scanner.nextInt();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter an integer.");
                        scanner.next();
                    }
                }
            }
            System.out.println("Point added successfully!");
            board.addAlignment(possibleAlignments.get(desiredAlignments - 1));
        }
        System.out.println();
    }

    public BoardFx getBoard() {
        return board;
    }
}
