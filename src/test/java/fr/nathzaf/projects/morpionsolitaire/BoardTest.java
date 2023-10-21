package fr.nathzaf.projects.morpionsolitaire;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

	@Test
	public void testAddValidPointTouchingMode() {
	    Board board = new Board(Mode.TOUCHING);
	    assertTrue(board.addPoint(new Point(0, 3)), "Should allow point to complete the alignment in Touching mode");
	}

	@Test
	public void testAddInvalidPointTouchingMode() {
	    Board board = new Board(Mode.TOUCHING);
	    Point existingPoint = new Point(1, 1); 
	    assertFalse(board.addPoint(existingPoint), "Should not allow an already existing point");
	}
}

