package fr.nathzaf.projects.morpionsolitaire;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {

    private final Board board;

    private final Scanner scanner;

    /**
     * Initializes a new game with the specified mode.
     *
     * @param mode The game mode, either TOUCHING or DISJOINT.
     */
    public Game(Mode mode) {
        this.board = new Board(mode);
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts and manages the game loop.
     */
    public void start() {
        while (!board.isGameOver()) {
            displayBoard();
            playTurn();
        }
        System.out.println("Game over! Your score is: " + board.getScore());
    }

    /**
     * Displays the current board state to the user.
     */
    private void displayBoard() {
        System.out.println(board);
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

        if (board.addPoint(point)) {
            System.out.println("Point added successfully!");
        } else {
            System.out.println("Invalid move! Try again.");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the game!");
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while(choice != 1 && choice != 2){
            System.out.println("Choose your mode: 1. TOUCHING, 2. DISJOINT");
            while (true) {
                try {
                    choice = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Please enter an integer.");
                    scanner.next();
                }
            }
        }

        Mode mode = (choice == 1) ? Mode.TOUCHING : Mode.DISJOINT;

        Game game = new Game(mode);
        game.start();
    }
}
