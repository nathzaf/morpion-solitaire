package fr.nathzaf.projects.morpionsolitaire;

import java.util.InputMismatchException;
import java.util.List;
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
        char choice = 0;

        while(!List.of(Mode.TOUCHING.getId(), Mode.DISJOINT.getId(), Mode.RANDOM_SOLVER.getId()).contains(choice)){
            System.out.println("Choose your mode: T for TOUCHING ; D for DISJOINT ; R for Random Solver (in T mode)");
            while (true) {
                try {
                    choice = scanner.next().charAt(0);
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Please enter an T ; D or R.");
                    scanner.next();
                }
            }
        }

        if(choice == Mode.TOUCHING.getId() || choice == Mode.DISJOINT.getId()) {
            Mode mode = choice == Mode.TOUCHING.getId() ? Mode.TOUCHING : Mode.DISJOINT;
            Game game = new Game(mode);
            game.start();
        }else if(choice == Mode.RANDOM_SOLVER.getId()){
            Solver solver = new RandomSolver(Mode.TOUCHING);
            solver.solve();
        }


    }
}
