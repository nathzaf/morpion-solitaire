package fr.nathzaf.projects.mavenproject;

import java.util.Scanner;

public class Game {
    private fr.nathzaf.projects.mavenproject.Board board;
    private Scanner scanner;
    /**
     * Initializes a new game with the specified mode.
     *
     * @param mode The game mode, either TOUCHING or DISJOINT.
     */
    public Game(Mode mode) {
        this.board = new fr.nathzaf.projects.mavenproject.Board(mode);
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
        System.out.println("Enter the X coordinate of the point you want to place: ");
        int x = scanner.nextInt();

        System.out.println("Enter the Y coordinate of the point you want to place: ");
        int y = scanner.nextInt();

        Point point = new Point(x, y);

        if (board.addPoint(point)) {
            System.out.println("Point added successfully!");
        } else {
            System.out.println("Invalid move! Try again.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the game!");
        System.out.println("Choose your mode: 1. TOUCHING, 2. DISJOINT");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        Mode mode = (choice == 1) ? Mode.TOUCHING : Mode.DISJOINT;

        Game game = new Game(mode);
        game.start();
    }
}
