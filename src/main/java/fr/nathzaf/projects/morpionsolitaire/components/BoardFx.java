package fr.nathzaf.projects.morpionsolitaire.components;

import com.google.common.collect.Sets;
import fr.nathzaf.projects.morpionsolitaire.game.Mode;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class BoardFx {

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
    public BoardFx(Mode mode) {
        if (mode == null)
            throw new NullPointerException("Mode can't be null.");
        points = new HashSet<>();
        alignments = new HashSet<>();
        this.gameMode = mode;
    }

    /**
     * Initializes the board with points.
     */
    private void initializeBoard() {
        int x = 0;
        int y = 0;

        if (INITIAL_SIZE <= 3) {
            throw new IllegalArgumentException("The width of the cross must be greater than 3.");
        }

        final int width = INITIAL_SIZE;
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
        if (point == null)
            throw new NullPointerException();
        if (points.contains(point) || isAlignedPoint(point)) return false;

        Set<Alignment> detectedAlignments = detectAlignments(point);
        return !detectedAlignments.isEmpty();
    }

    /**
     * Adds a point to the board if the move is valid.
     *
     * @param point the point to be added
     * @return the set of all possible alignment with this point
     */
    public Set<Alignment> addPoint(Point point) {
        if (point == null)
            throw new NullPointerException("Adding a null point");
        if (isValidMove(point)) {
            score++;
            points.add(new Point(point, score));
            return detectAlignments(point);
        }
        return new HashSet<>();
    }

    public void addAlignment(Alignment alignment) {
        if (alignment == null)
            throw new NullPointerException("Adding a null alignment");
        alignments.add(new Alignment(alignment, score));
    }

    /**
     * Detects an alignment for the given point.
     *
     * @param point the point for which alignment needs to be detected
     * @return a set of points that are aligned, null if none found
     */
    private Set<Alignment> detectAlignments(Point point) {
        if (point == null)
            throw new NullPointerException();
        Set<Alignment> possibleAlignments = new HashSet<>();

        for (Direction direction : Direction.values()) {
            possibleAlignments.addAll(hasAlignment(point, direction));
        }

        return possibleAlignments;
    }

    /**
     * Checks for alignment with a given direction and required count.
     *
     * @param point     the starting point
     * @param direction the direction
     * @return a set of Alignments that are valid
     */
    private Set<Alignment> hasAlignment(Point point, Direction direction) {
        if (point == null || direction == null)
            throw new NullPointerException();
        Set<Alignment> possibleAlignments = new HashSet<>();
        Set<Point> alignedPoints = new HashSet<>();
        int dx = direction.getDx();
        int dy = direction.getDy();
        for (int j = 0; j <= 4; j++) {
            for (int i = -4 + j; i <= j; i++) {
                Point alignedPoint = new Point(point.getX() + i * dx, point.getY() + i * dy);
                if (points.contains(alignedPoint) || alignedPoint.equals(point)) {
                    alignedPoints.add(alignedPoint);
                }
                if (alignedPoints.size() == REQUIRED) {
                    Alignment possibleAlignment = new Alignment(alignedPoints, direction);
                    if (!alignments.contains(possibleAlignment)) {
                        boolean valid = true;
                        for (Alignment alignment : alignments) {
                            if (Sets.intersection(alignment.getPoints(), possibleAlignment.getPoints()).size() >= gameMode.getMaxCommonPoints()) {
                                if (gameMode.equals(Mode.TOUCHING)
                                        || (gameMode.equals(Mode.DISJOINT) && alignment.getDirection().equals(possibleAlignment.getDirection()))) {
                                    valid = false;
                                    break;
                                }
                            }
                        }
                        if (valid)
                            possibleAlignments.add(possibleAlignment);
                    }
                }
            }
            alignedPoints.clear();
        }
        return possibleAlignments;
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

    public Point undo() {
        Point undoPoint = null;
        Alignment undoAlignmenet = null;
        for(Point point : points) {
            if(point.getScore() == score)
                undoPoint = point;
        }
        for(Alignment alignment : alignments) {
            if(alignment.getScore() == score)
                undoAlignmenet = alignment;
        }
        if(undoPoint == null || undoAlignmenet == null)
            throw new IllegalStateException("Can't undo.");
        score--;
        points.remove(undoPoint);
        alignments.remove(undoAlignmenet);
        return undoPoint;
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
        return points;
    }

    public Mode getGameMode() {
        return gameMode;
    }

    public Set<Alignment> getAlignments() {
        return alignments;
    }

    private boolean isAlignedPoint(Point point) {
        for (Alignment alignment : alignments) {
            if (alignment.getPoints().contains(point))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        Set<Point> possibleMoves = getPossibleMoves();
        Set<Point> allPoints = new HashSet<>(points);
        for (Alignment alignment : alignments)
            allPoints.addAll(alignment.getPoints());
        allPoints.addAll(possibleMoves);

        int minX = allPoints.stream().mapToInt(Point::getX).min().orElse(0) - 1;
        int maxX = allPoints.stream().mapToInt(Point::getX).max().orElse(0) + 1;
        int minY = allPoints.stream().mapToInt(Point::getY).min().orElse(0) - 1;
        int maxY = allPoints.stream().mapToInt(Point::getY).max().orElse(0) + 1;

        StringBuilder builder = new StringBuilder();
        for (int i = maxY; i >= minY; i--) {
            for (int j = minX; j <= maxX; j++) {
                Point currentPoint = new Point(j, i);
                if (isAlignedPoint(currentPoint)) {
                    builder.append("O");
                } else if (points.contains(currentPoint)) {
                    builder.append("X");
                } else if (possibleMoves.contains(currentPoint)) {
                    builder.append("?");
                } else {
                    builder.append(".");
                }
                builder.append("  ");
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
