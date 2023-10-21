package fr.nathzaf.projects.morpionsolitaire;

import java.util.HashSet;
import java.util.Set;

public class Board {

    private final Set<Point> points;

    private final Set<Alignment> alignments;

    private final Mode gameMode;

    private int score = 0;

    private static final int INITIAL_SIZE = 4;


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
        boolean isAlignedPoint = false;
        for(Alignment alignment : alignments)
            if(alignment.getPoints().contains(point)){
                isAlignedPoint = true;
                break;
            }
        if (points.contains(point) || isAlignedPoint) return false;

        Alignment detectedAlignment = detectAlignment(point);
        if (detectedAlignment == null) return false;

        for(Alignment alignment : alignments){
            long count = detectedAlignment.getPoints().stream()
                    .filter(alignment.getPoints()::contains)
                    .count();
            if (count > 2)
                return false;
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
        if (hasAlignment(point, 0, 1, 5) != null)
            return hasAlignment(point, 0, 1, 5);
        if (hasAlignment(point, 1, 0, 5) != null)
            return hasAlignment(point, 1, 0, 5);
        if (hasAlignment(point, 1, 1, 5) != null)
            return hasAlignment(point, 1, 1, 5);
        if (hasAlignment(point, 1, -1, 5) != null)
            return hasAlignment(point, 1, -1, 5);

        if (hasAlignment(point, 0, -1, 5) != null)
            return hasAlignment(point, 0, -1, 5);
        if (hasAlignment(point, -1, 0, 5) != null)
            return hasAlignment(point, -1, 0, 5);
        if (hasAlignment(point, -1, -1, 5) != null)
            return hasAlignment(point, -1, -1, 5);
        if (hasAlignment(point, -1, 1, 5) != null)
            return hasAlignment(point, -1, 1, 5);
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
        for (int i = -4; i <= 4; i++) {
            if (i == 0) {
                alignedPoints.add(point);
                continue;
            }
            Point alignedPoint = new Point(point.getX() + i * dx, point.getY() + i * dy);
            if (points.contains(alignedPoint)) {
                alignedPoints.add(alignedPoint);
            } else {
                alignedPoints.clear();
            }

            if (alignedPoints.size() == required) {
                for(Alignment alignment : alignments){
                    if(alignment.getPoints().containsAll(alignedPoints)){
                        return null;
                    }
                }
                return new Alignment(alignedPoints);
            }
        }
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
                boolean isAligned = false;
                for(Alignment alignment : alignments){
                    if (alignment.getPoints().contains(currentPoint)){
                        builder.append("O ");
                        isAligned = true;
                        break;
                    }
                }
                if (points.contains(currentPoint) && !isAligned) {
                    builder.append("X ");
                } else if (possibleMoves.contains(currentPoint) && !isAligned) {
                    builder.append("? ");
                } else if (!isAligned) {
                    builder.append(". ");
                }
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
