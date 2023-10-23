package fr.nathzaf.projects.morpionsolitaire;

import com.google.common.collect.ImmutableSet;
import java.util.Objects;
import java.util.Set;

public class Alignment {
    private final ImmutableSet<Point> points;

    private final Point start;

    private final Point end;

    private final Direction direction;

    public Alignment(Set<Point> points, Point start, Point end, Direction direction) {
        if (points.size() != 5) {
            throw new IllegalArgumentException("An alignment must be composed of 5 points.");
        }
        this.points = ImmutableSet.copyOf(points);
        this.start = start;
        this.end = end;
        this.direction = direction;
    }

    public ImmutableSet<Point> getPoints() {
        return points;
    }

    public Direction getDirection() {
        return direction;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alignment alignment = (Alignment) o;
        return Objects.equals(points, alignment.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }

    @Override
    public String toString() {
        return "Alignment{" +
                "points=" + points +
                ", start=" + start +
                ", end=" + end +
                ", direction=" + direction +
                '}';
    }
}
