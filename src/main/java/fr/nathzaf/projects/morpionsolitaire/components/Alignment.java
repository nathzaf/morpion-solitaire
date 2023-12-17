package fr.nathzaf.projects.morpionsolitaire.components;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Alignment {

    private final ImmutableSet<Point> points;

    private final Direction direction;

    private final int score;

    public static final int ALIGNMENT_SIZE = 5;

    /**
     * Constructor for 'default' alignment, especially for playable alignment test.
     *
     * @param points the set of points of the alignment
     * @param direction the direction of the alignment
     */
    public Alignment(Set<Point> points, Direction direction) {
        if (points.size() != ALIGNMENT_SIZE)
            throw new IllegalArgumentException("An alignment must be composed of 5 points.");
        if (direction == null)
            throw new NullPointerException("direction can't be null.");
        this.points = ImmutableSet.copyOf(points);
        this.direction = direction;
        this.score = -1;
    }

    /**
     * Constructor for 'played' alignment.
     *
     * @param alignment the alignment to be copied
     * @param score the associated score
     */
    public Alignment(Alignment alignment, int score) {
        if (alignment == null)
            throw new NullPointerException("alignment can't be null.");
        this.points = alignment.getPoints();
        this.direction = alignment.getDirection();
        this.score = score;
    }

    /**
     * Gets the list of 2 extremities of an alignment.
     *
     * @return the list of 2 extremities of an alignment
     */
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

        if(minPoint == null || maxPoint == null || minPoint.equals(maxPoint))
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
    public String toString(){
        return "Alignment{" +
                "points=" + points +
                ", direction=" + direction +
                '}';
    }
}
