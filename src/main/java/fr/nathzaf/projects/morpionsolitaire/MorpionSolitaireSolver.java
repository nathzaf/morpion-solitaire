package fr.nathzaf.projects.morpionsolitaire;

import java.util.Random;
import java.util.Set;

public class MorpionSolitaireSolver {
    
    private final Board board;
    private Random random = new Random();

    public MorpionSolitaireSolver(Mode mode) {
        this.board = new Board(mode);
    }

    public void solveRandomly() {
        while (!board.isGameOver()) {
            Set<Point> possibleMoves = board.getPossibleMoves();
            
            if(possibleMoves.isEmpty()) {
                break;
            }

            int index = random.nextInt(possibleMoves.size());
            Point randomMove = (Point) possibleMoves.toArray()[index];

            board.addPoint(randomMove);
        }
        System.out.println(board);
        System.out.println("Game over! Score: " + board.getScore());
    }

    public static void main(String[] args) {
        MorpionSolitaireSolver solver = new MorpionSolitaireSolver(Mode.TOUCHING);
        solver.solveRandomly();
    }
}
