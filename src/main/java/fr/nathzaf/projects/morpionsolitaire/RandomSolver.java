package fr.nathzaf.projects.morpionsolitaire;

import java.util.Random;
import java.util.Set;

public class RandomSolver implements Solver {
    
    private final Board board;
    private final Random random = new Random();

    public RandomSolver(Mode mode) {
        this.board = new Board(mode);
    }

    @Override
    public void solve() {
        System.out.println("Solving in progress, please wait...");
        while (!board.isGameOver()) {
            Set<Point> possibleMoves = board.getPossibleMoves();
            
            if(possibleMoves.isEmpty()) {
                break;
            }

            int index = random.nextInt(possibleMoves.size());
            Point randomMove = possibleMoves.stream().toList().get(index);

            board.addPoint(randomMove);
        }
        System.out.println(board);
        System.out.println("Game over! Score: " + board.getScore());
    }

}
