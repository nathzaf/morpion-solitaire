package fr.nathzaf.projects.morpionsolitaire.components;

import fr.nathzaf.projects.morpionsolitaire.game.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void addValidPointTouchingModeTest() {
        Board board = new Board(Mode.TOUCHING);
        board.getPoints().add(new Point(3,6));
        board.getPoints().add(new Point(4,6));
        board.getPoints().add(new Point(5,6));
        board.getPoints().add(new Point(6,6));
        Set<Alignment> alignments = board.addPoint(new Point(7, 6));
        assertFalse(alignments.isEmpty());
    }

    @Test
    public void addInvalidPointTouchingModeTest() {
        Board board = new Board(Mode.TOUCHING);
        Set<Alignment> alignments = board.addPoint(new Point(1, 1));
        assertTrue(alignments.isEmpty());
    }

    @Test
    public void getPossibleMovesTest() {
        Board board = new Board(Mode.TOUCHING);
        board.getPoints().add(new Point(3,6));
        board.getPoints().add(new Point(4,6));
        board.getPoints().add(new Point(5,6));
        board.getPoints().add(new Point(6,6));
        assertEquals(Set.of(new Point(2, 6), new Point(7, 6)), board.getPossibleMoves());
    }

    @Test
    public void undoTest() {
        Board board = new Board(Mode.TOUCHING);
        board.getPoints().add(new Point(3,6));
        board.getPoints().add(new Point(4,6));
        board.getPoints().add(new Point(5,6));
        board.getPoints().add(new Point(6,6));

        board.getPoints().add(new Point(3,7));
        board.getPoints().add(new Point(3,8));
        board.getPoints().add(new Point(3,9));

        board.addAlignment(board.addPoint(new Point(7,6)).stream().toList().get(0));
        board.addAlignment(board.addPoint(new Point(3,10)).stream().toList().get(0));

        Assertions.assertEquals(2, board.getScore());

        board.undo();

        Assertions.assertEquals(1, board.getScore());
        Assertions.assertFalse(board.getPoints().contains(new Point(3,10)));
    }
}

