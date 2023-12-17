package fr.nathzaf.projects.morpionsolitaire.solver;

import fr.nathzaf.projects.morpionsolitaire.components.Alignment;
import fr.nathzaf.projects.morpionsolitaire.components.Board;
import fr.nathzaf.projects.morpionsolitaire.components.Point;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomSolver implements Solver {

    private final Board board;

    public RandomSolver(Board board) {
        this.board = board;
    }

    /**
     * Solve the game by playing random points, and random alignment if there are multiple choices.
     */
    @Override
    public void solve() {
        Random random = new Random();
        while (board.getPossibleMoves().size() > 0) {
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
    }

}
