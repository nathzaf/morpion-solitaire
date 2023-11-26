package fr.nathzaf.projects.morpionsolitaire.components;

import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Alignment {
    private final ImmutableSet<Point> points;

    private final Direction direction;

    private final int score;

    public Alignment(Set<Point> points, Direction direction) {
        if (points.size() != 5) {
            throw new IllegalArgumentException("An alignment must be composed of 5 points.");
        }
        this.points = ImmutableSet.copyOf(points);
        this.direction = direction;
        score = -1;
    }

    public Alignment(Alignment alignment, int score) {
        this.points = alignment.getPoints();
        this.direction = alignment.getDirection();
        this.score = score;
    }

    public List<Point> getExtremities() {
        Point minPoint = null;
        Point maxPoint = null;

        for(Point point : points) {
            if(minPoint == null && maxPoint == null) {
                minPoint = point;
                maxPoint = point;
                continue;
            }
            switch (direction) {
                case VERTICAL:
                    if (point.getY() < minPoint.getY())
                        minPoint = point;
                    if (point.getY() > maxPoint.getY())
                        maxPoint = point;
                    break;
                case HORIZONTAL:
                case DIAGONAL_TOP:
                case DIAGONAL_BOTTOM:
                    if (point.getX() < minPoint.getX())
                        minPoint = point;
                    if (point.getX() > maxPoint.getX())
                        maxPoint = point;
                    break;
            }
        }

        if(minPoint == null && maxPoint == null || minPoint.equals(maxPoint))
            throw new IllegalStateException("Two different extremities must have been found.");

        return List.of(minPoint, maxPoint);
    }

    public ImmutableSet<Point> getPoints() {
        return points;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getScore() {
        return score;
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
                ", direction=" + direction +
                '}';
    }
}
