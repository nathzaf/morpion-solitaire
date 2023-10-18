package fr.nathzaf.projects.mavenproject;

import java.util.HashSet;
import java.util.Set;

public class Board {
    private Set<Point> points;
    private Mode gameMode;
    private static final int INITIAL_SIZE = 5;

    public Board(Mode mode) {
        points = new HashSet<>();
        this.gameMode = mode;
        initializeBoard();
    }

    private void initializeBoard() {
        int offset = INITIAL_SIZE / 2;
        for (int i = -offset; i <= offset; i++) {
            for (int j = -offset; j <= offset; j++) {
                if (i == 0 || j == 0) {
                    points.add(new Point(i, j));
                }
            }
        }
    }

    public boolean addPoint(Point point) {
        if (isValidMove(point)) {
            points.add(point);
            return true;
        }
        return false;
    }

    private boolean isValidMove(Point point) {
        if (points.contains(point)) return false;
        
        Direction alignment = detectAlignment(point);
        if (alignment == null) return false;

        switch (gameMode) {
            case TOUCHING:
                return hasAdjacentPoint(point);
            //case DISJOINT:

        }

        return false; 
    }

    private boolean hasAdjacentPoint(Point point) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;  // Skip the point itself
                if (points.contains(new Point(point.getX() + dx, point.getY() + dy))) {
                    return true;
                }
            }
        }
        return false;
    }

    private Direction detectAlignment(Point point) {
        if (hasAlignment(point, 0, 1, 4)) return Direction.VERTICAL;
        if (hasAlignment(point, 1, 0, 4)) return Direction.HORIZONTAL;
        if (hasAlignment(point, 1, 1, 4)) return Direction.DIAGONAL_ASC;
        if (hasAlignment(point, 1, -1, 4)) return Direction.DIAGONAL_DESC;

        return null;
    }

    private boolean hasAlignment(Point point, int dx, int dy, int required) {
        int count = 0;
        for (int i = -4; i <= 4; i++) {
            if (i == 0) continue;  
            if (points.contains(new Point(point.getX() + i * dx, point.getY() + i * dy))) {
                count++;
                if (count == required) return true;  
            } else {
                count = 0;
            }
        }
        return false;
    }

    public Set<Point> getPoints() {
        return new HashSet<>(points);
    }

    @Override
    public String toString() {
        int minSize = -INITIAL_SIZE;
        int maxSize = INITIAL_SIZE;
        StringBuilder builder = new StringBuilder();
        for (int i = minSize; i <= maxSize; i++) {
            for (int j = minSize; j <= maxSize; j++) {
                if (points.contains(new Point(j, i))) {
                    builder.append("X ");
                } else {
                    builder.append(". ");
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}

