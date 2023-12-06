package fr.nathzaf.projects.morpionsolitaire.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PointTest {

    @Test
    public void generateCircleIdTest() {
        Point point = new Point(8, 1);
        Assertions.assertEquals("#x8y1", point.generateCircleId());
    }

    @Test
    public void pointConstructorTestKo() {
        NullPointerException e = Assertions.assertThrows(NullPointerException.class, () -> new Point(null, 5));
        Assertions.assertEquals("point can't be null.", e.getMessage());
    }

}
