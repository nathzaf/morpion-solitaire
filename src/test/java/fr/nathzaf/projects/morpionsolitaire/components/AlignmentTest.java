package fr.nathzaf.projects.morpionsolitaire.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class AlignmentTest {

    @Test
    public void alignmentConstructorTestKo() {
        IllegalArgumentException e1 = Assertions.assertThrows(IllegalArgumentException.class, () -> new Alignment(Set.of(new Point(1, 1),
                new Point(1, 2),
                new Point(2, 3),
                new Point(0, 0)), Direction.VERTICAL));
        Assertions.assertEquals("An alignment must be composed of 5 points.", e1.getMessage());

        NullPointerException e2 = Assertions.assertThrows(NullPointerException.class, () -> new Alignment(null, 5));
        Assertions.assertEquals("alignment can't be null.", e2.getMessage());

        NullPointerException e3 = Assertions.assertThrows(NullPointerException.class, () -> new Alignment(Set.of(new Point(1, 1),
                new Point(1, 2),
                new Point(2, 3),
                new Point(0, 0),
                new Point(3, 0)), null));
        Assertions.assertEquals("direction can't be null.", e3.getMessage());
    }

    @Test
    public void alignmentEqualsTest() {
        Alignment alignment1 = new Alignment(Set.of(new Point(1, 1),
                new Point(1, 2),
                new Point(2, 3),
                new Point(0, 0),
                new Point(0, 1)), Direction.VERTICAL);

        Alignment alignment2 = new Alignment(Set.of(new Point(0, 1),
                new Point(1, 1),
                new Point(2, 3),
                new Point(0, 0),
                new Point(1, 2)), Direction.VERTICAL);

        Alignment alignment3 = new Alignment(Set.of(new Point(-1, -1),
                new Point(1, 2),
                new Point(2, 3),
                new Point(0, 0),
                new Point(0, 1)), Direction.VERTICAL);

        Assertions.assertEquals(alignment1, alignment2);
        Assertions.assertNotEquals(alignment1, alignment3);
        Assertions.assertNotEquals(alignment2, alignment3);
    }

    @Test
    public void getExtremitiesTest() {
        Alignment alignment1 = new Alignment(Set.of(new Point(1, 1),
                new Point(1, 2),
                new Point(1, 3),
                new Point(1, 4),
                new Point(1, 5)), Direction.VERTICAL);

        Alignment alignment2 = new Alignment(Set.of(new Point(1, 1),
                new Point(2, 1),
                new Point(3, 1),
                new Point(4, 1),
                new Point(5, 1)), Direction.HORIZONTAL);

        Alignment alignment3 = new Alignment(Set.of(new Point(1, 1),
                new Point(2, 2),
                new Point(3, 3),
                new Point(4, 4),
                new Point(5, 5)), Direction.DIAGONAL_TOP);

        Alignment alignment4 = new Alignment(Set.of(new Point(1, -1),
                new Point(2, -2),
                new Point(3, -3),
                new Point(4, -4),
                new Point(5, -5)), Direction.DIAGONAL_BOTTOM);

        Assertions.assertTrue(List.of(new Point(1, 1), new Point(1, 5)).containsAll(alignment1.getExtremities()));
        Assertions.assertTrue(List.of(new Point(1, 1), new Point(5, 1)).containsAll(alignment2.getExtremities()));
        Assertions.assertTrue(List.of(new Point(1, 1), new Point(5, 5)).containsAll(alignment3.getExtremities()));
        Assertions.assertTrue(List.of(new Point(1, -1), new Point(5, -5)).containsAll(alignment4.getExtremities()));
    }
}
