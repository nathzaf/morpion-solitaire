package fr.nathzaf.projects.morpionsolitaire.solver;

import fr.nathzaf.projects.morpionsolitaire.components.Alignment;
import fr.nathzaf.projects.morpionsolitaire.components.Board;
import fr.nathzaf.projects.morpionsolitaire.components.Point;
import fr.nathzaf.projects.morpionsolitaire.game.Mode;

import java.util.List;
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

            if (possibleMoves.isEmpty()) {
                break;
            }

            int index = random.nextInt(possibleMoves.size());
            Point randomMove = possibleMoves.stream().toList().get(index);

            List<Alignment> possibleAlignments = board.addPoint(randomMove).stream().toList();
            if (possibleAlignments.isEmpty()) {
                throw new IllegalStateException("An error has occurred");
            } else if (possibleAlignments.size() == 1) {
                board.addAlignment(possibleAlignments.get(0));
            } else {
                int randomAlignmentIndex = random.nextInt(possibleAlignments.size());
                board.addAlignment(possibleAlignments.get(randomAlignmentIndex));
            }
        }
        System.out.println(board);
        System.out.println("Game over! Score: " + board.getScore());
    }

}
