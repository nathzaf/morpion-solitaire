package fr.nathzaf.projects.morpionsolitaire;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {

    private final Set<Point> points;

    private final Set<Alignment> alignments;

    private final Mode gameMode;

    private int score = 0;

    private static final int INITIAL_SIZE = 4;

    private static final int REQUIRED = 5;


    /**
     * Constructs a new board with a given mode.
     *
     * @param mode the game mode for this board
     */
    public Board(Mode mode) {
        points = new HashSet<>();
        alignments = new HashSet<>();
        this.gameMode = mode;
        initializeBoard();
    }

    /**
     * Initializes the board with points.
     */
    private void initializeBoard() {
        int x = 0;
        int y = 0;
        int width = INITIAL_SIZE;

        if (width <= 3) {
            throw new IllegalArgumentException("The width of the cross must be greater than 3.");
        }

        for (int i = 0; i < width; ++i) {
            points.add(new Point(x + i, y));
            points.add(new Point(x + i - width + 1, y + width - 1));
            points.add(new Point(x + i + width - 1, y + width - 1));
            points.add(new Point(x + i - width + 1, y + 2 * width - 2));
            points.add(new Point(x + i + width - 1, y + 2 * width - 2));
            points.add(new Point(x + i, y + 3 * width - 3));
            points.add(new Point(x, y + i));
            points.add(new Point(x + width - 1, y + i));
            points.add(new Point(x - width + 1, y + i + width - 1));
            points.add(new Point(x + 2 * width - 2, y + i + width - 1));
            points.add(new Point(x, y + i + 2 * width - 2));
            points.add(new Point(x + width - 1, y + i + 2 * width - 2));
        }
    }

    /**
     * Checks if the given point move is valid.
     *
     * @param point the point to be checked
     * @return true if the move is valid, false otherwise
     */
    private boolean isValidMove(Point point) {
        if (points.contains(point) || isAlignedPoint(point)) return false;

        Alignment detectedAlignment = detectAlignment(point);
        if (detectedAlignment == null) return false;

        if (gameMode == Mode.TOUCHING) {
            for (Alignment alignment : alignments) {
                if (alignment.getDirection() == detectedAlignment.getDirection()) {
                    long count = detectedAlignment.getPoints().stream()
                            .filter(alignment.getPoints()::contains)
                            .count();
                    if (count > 2) {
                        return false;
                    }
                }
            }
        } else if (gameMode == Mode.DISJOINT) {
            for (Alignment alignment : alignments) {
                if (alignment.getDirection() == detectedAlignment.getDirection()) {
                    boolean hasCommonExtremities = List.of(alignment.getStart(), alignment.getEnd()).contains(detectedAlignment.getEnd())
                            || List.of(alignment.getStart(), alignment.getEnd()).contains(detectedAlignment.getStart());
                    if (hasCommonExtremities) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Adds a point to the board if the move is valid.
     *
     * @param point the point to be added
     * @return true if the point was added, false otherwise
     */
    public boolean addPoint(Point point) {
        if (isValidMove(point)) {
            points.add(point);
            Alignment alignment = detectAlignment(point);
            if (alignment != null) {
                alignments.add(alignment);
            }
            score++;
            return true;
        }
        return false;
    }

    /**
     * Detects an alignment for the given point.
     *
     * @param point the point for which alignment needs to be detected
     * @return a set of points that are aligned, null if none found
     */
    private Alignment detectAlignment(Point point) {
        Alignment alignment;

        alignment = hasAlignment(point, 0, 1, REQUIRED);
        if (alignment != null) return alignment;

        alignment = hasAlignment(point, 1, 0, REQUIRED);
        if (alignment != null) return alignment;

        alignment = hasAlignment(point, 1, 1, REQUIRED);
        if (alignment != null) return alignment;

        alignment = hasAlignment(point, 1, -1, REQUIRED);
        if (alignment != null) return alignment;

        alignment = hasAlignment(point, 0, -1, REQUIRED);
        if (alignment != null) return alignment;

        alignment = hasAlignment(point, -1, 0, REQUIRED);
        if (alignment != null) return alignment;

        alignment = hasAlignment(point, -1, -1, REQUIRED);
        if (alignment != null) return alignment;

        alignment = hasAlignment(point, -1, 1, REQUIRED);
        if (alignment != null) return alignment;

        return null;
    }

    /**
     * Checks for alignment with a given direction and required count.
     *
     * @param point    the starting point
     * @param dx       the x-direction
     * @param dy       the y-direction
     * @param required the number of aligned points required
     * @return a set of points that are aligned, null if none found
     */
    private Alignment hasAlignment(Point point, int dx, int dy, int required) {
        Set<Point> alignedPoints = new HashSet<>();
        Direction direction = detectDirection(dx, dy);
        for (int i = -4; i <= 4; i++) {
            Point alignedPoint = new Point(point.getX() + i * dx, point.getY() + i * dy);
            if (points.contains(alignedPoint) || alignedPoint.equals(point)) {
                alignedPoints.add(alignedPoint);
            } else {
                if (alignedPoints.size() < required) {
                    alignedPoints.clear();
                }
            }
            List<Point> extremities = getExtremities(points, direction);
            if (alignedPoints.size() == required) {
                for(Alignment alignment : alignments){
                    if(alignment.equals(new Alignment(alignedPoints, extremities.get(0), extremities.get(1), direction))){
                        return null;
                    }
                }
                return new Alignment(alignedPoints, extremities.get(0), extremities.get(1), direction);
            }
        }
        return null;
    }

    private List<Point> getExtremities(Set<Point> points, Direction direction) {
        return switch (direction) {
            case VERTICAL -> List.of(getMaxY(points), getMinY(points));
            case HORIZONTAL, DIAGONAL_TOP, DIAGONAL_BOTTOM -> List.of(getMaxX(points), getMinX(points));
        };
    }

    private Point getMaxX(Set<Point> points) {
        Point maxX = null;
        for(Point point : points) {
            if(maxX == null)
                maxX = point;
            else if (maxX.getX() > point.getX())
                maxX = point;
        }
        return maxX;
    }

    private Point getMinX(Set<Point> points) {
        Point minX = null;
        for(Point point : points) {
            if(minX == null)
                minX = point;
            else if (minX.getX() < point.getX())
                minX = point;
        }
        return minX;
    }

    private Point getMaxY(Set<Point> points) {
        Point maxY = null;
        for(Point point : points) {
            if(maxY == null)
                maxY = point;
            else if (maxY.getY() > point.getY())
                maxY = point;
        }
        return maxY;
    }

    private Point getMinY(Set<Point> points) {
        Point minY = null;
        for(Point point : points) {
            if(minY == null)
                minY = point;
            else if (minY.getY() < point.getY())
                minY = point;
        }
        return minY;
    }

    private Direction detectDirection(int dx, int dy) {
        if ((dx == 0 && dy == 1) || (dx == 0 && dy == -1))
            return Direction.VERTICAL;
        if ((dx == 1 && dy == 0) || (dx == -1 && dy == 0))
            return Direction.HORIZONTAL;
        if ((dx == 1 && dy == 1) || (dx == -1 && dy == -1))
            return Direction.DIAGONAL_TOP;
        if ((dx == 1 && dy == -1) || (dx == -1 && dy == 1))
            return Direction.DIAGONAL_BOTTOM;
        return null;
    }

    /**
     * Determines if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        int searchRadius = 4;

        for (Point existingPoint : points) {
            for (int dx = -searchRadius; dx <= searchRadius; dx++) {
                for (int dy = -searchRadius; dy <= searchRadius; dy++) {
                    Point potentialPoint = new Point(existingPoint.getX() + dx, existingPoint.getY() + dy);

                    if (points.contains(potentialPoint)) {
                        continue;
                    }

                    if (isValidMove(potentialPoint)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Gets the possible moves
     *
     * @return a set of points that can be played
     */
    public Set<Point> getPossibleMoves() {
        Set<Point> possibleMoves = new HashSet<>();
        int searchRadius = 4;

        for (Point existingPoint : points) {
            for (int dx = -searchRadius; dx <= searchRadius; dx++) {
                for (int dy = -searchRadius; dy <= searchRadius; dy++) {
                    Point potentialPoint = new Point(existingPoint.getX() + dx, existingPoint.getY() + dy);
                    if (!points.contains(potentialPoint) && isValidMove(potentialPoint)) {
                        possibleMoves.add(potentialPoint);
                    }
                }
            }
        }

        return possibleMoves;
    }

    /**
     * Returns the current score of the board.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Resets the board to its initial state.
     */
    public void reset() {
        points.clear();
        alignments.clear();
        initializeBoard();
    }

    public Set<Point> getPoints() {
        return new HashSet<>(points);
    }

    private boolean isAlignedPoint(Point point){
        for(Alignment alignment : alignments) {
            if(alignment.getPoints().contains(point))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        Set<Point> possibleMoves = getPossibleMoves();
        Set<Point> allPoints = new HashSet<>(points);
        for(Alignment alignment : alignments)
            allPoints.addAll(alignment.getPoints());
        allPoints.addAll(possibleMoves);

        int minX = allPoints.stream().mapToInt(Point::getX).min().orElse(0) - 1;
        int maxX = allPoints.stream().mapToInt(Point::getX).max().orElse(0) + 1;
        int minY = allPoints.stream().mapToInt(Point::getY).min().orElse(0) - 1;
        int maxY = allPoints.stream().mapToInt(Point::getY).max().orElse(0) + 1;

        StringBuilder builder = new StringBuilder();
        for (int i = minY; i <= maxY; i++) {
            for (int j = minX; j <= maxX; j++) {
                Point currentPoint = new Point(j, i);
                if (isAlignedPoint(currentPoint)){
                    builder.append("O");
                } else if (points.contains(currentPoint)) {
                    builder.append("X");
                } else if (possibleMoves.contains(currentPoint)) {
                    builder.append("?");
                } else {
                    builder.append(".");
                }
                builder.append(" ");
            }
            builder.append("\n");
        }

        builder.append("\nPossible moves:\n");
        for (Point move : possibleMoves) {
            builder.append("[").append(move.getX()).append(", ").append(move.getY()).append("]\n");
        }

        return builder.toString();
    }
}
