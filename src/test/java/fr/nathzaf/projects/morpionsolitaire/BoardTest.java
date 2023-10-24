package fr.nathzaf.projects.morpionsolitaire;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

	@Test
	public void testAddValidPointTouchingMode() {
	    Board board = new Board(Mode.TOUCHING);
		Set<Alignment> alignments = board.addPoint(new Point(2, 3));
		assertFalse(alignments.isEmpty());
	}

	@Test
	public void testAddInvalidPointTouchingMode() {
	    Board board = new Board(Mode.TOUCHING);
		Set<Alignment> alignments = board.addPoint(new Point(1, 1));
		assertTrue(alignments.isEmpty());

	}
}

