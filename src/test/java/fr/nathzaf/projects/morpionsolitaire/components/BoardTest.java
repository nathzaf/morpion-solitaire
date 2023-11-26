package fr.nathzaf.projects.morpionsolitaire.components;

import fr.nathzaf.projects.morpionsolitaire.game.Mode;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

    @Test
    public void testAddValidPointTouchingMode() {
        Board board = new Board(Mode.TOUCHING);
        board.getPoints().add(new Point(3,6));
        board.getPoints().add(new Point(4,6));
        board.getPoints().add(new Point(5,6));
        board.getPoints().add(new Point(6,6));
        Set<Alignment> alignments = board.addPoint(new Point(7, 6));
        assertFalse(alignments.isEmpty());
    }

    @Test
    public void testAddInvalidPointTouchingMode() {
        Board board = new Board(Mode.TOUCHING);
        Set<Alignment> alignments = board.addPoint(new Point(1, 1));
        assertTrue(alignments.isEmpty());

    }
}

