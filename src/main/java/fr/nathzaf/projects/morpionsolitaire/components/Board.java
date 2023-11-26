package fr.nathzaf.projects.morpionsolitaire.components;

import com.google.common.collect.Sets;
import fr.nathzaf.projects.morpionsolitaire.game.Mode;

import java.util.HashSet;
import java.util.Set;

public class Board {

    private final Set<Point> points;

    private final Set<Alignment> alignments;

    private final Mode gameMode;

    private int score = 0;

    private static final int REQUIRED = 5;

    /**
     * Constructs a new board with a given mode.
     *
     * @param mode the game mode for this board
     */
    public Board(Mode mode) {
        if (mode == null)
            throw new NullPointerException("Mode can't be null.");
        points = new HashSet<>();
        alignments = new HashSet<>();
        this.gameMode = mode;
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

    private boolean isAlignedPoint(Point point) {
        for (Alignment alignment : alignments) {
            if (alignment.getPoints().contains(point))
                return true;
        }
        return false;
    }

    /**
     * Returns the current score of the board.
     *
     * @return the score
     */
    public int getScore() {
        return score;
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
}
