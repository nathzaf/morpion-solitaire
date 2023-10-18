package fr.nathzaf.projects.mavenproject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

	@Test
	public void testAddValidPointTouchingMode() {
	    Board board = new Board(Mode.TOUCHING);
	    board.addPoint(new Point(0, 1));
	    board.addPoint(new Point(0, 2));
	    board.addPoint(new Point(0, 3));
	    board.addPoint(new Point(0, 4));
	    assertTrue(board.addPoint(new Point(0, 5)), "Should allow point to complete the alignment in Touching mode");
	}

	@Test
	public void testAddInvalidPointTouchingMode() {
	    Board board = new Board(Mode.TOUCHING);
	    Point existingPoint = new Point(0, 0);  // Ce point fait partie de l'initialisation
	    assertFalse(board.addPoint(existingPoint), "Should not allow an already existing point");
	}

    @Test
    public void testAlignmentDetection() {
        Board board = new Board(Mode.TOUCHING);
        board.addPoint(new Point(1, 2));
        board.addPoint(new Point(1, 3));
        board.addPoint(new Point(1, 4));
        board.addPoint(new Point(1, 5));
        board.addPoint(new Point(1, 6));

        assertFalse(board.addPoint(new Point(1, 7)), "Should not allow point in Touching mode as it will touch the existing alignment");
    }
}

