package fr.nathzaf.projects.morpionsolitaire.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class AlignmentTest {

    @Test
    public void alignmentConstructorTest() {
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> new Alignment(Set.of(new Point(1, 1),
                new Point(1, 2),
                new Point(2, 3),
                new Point(0, 0)), Direction.VERTICAL));
        Assertions.assertEquals("An alignment must be composed of 5 points.", e.getMessage());
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
}
