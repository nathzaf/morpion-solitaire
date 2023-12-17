package fr.nathzaf.projects.morpionsolitaire.components;

import com.google.common.collect.Sets;
import fr.nathzaf.projects.morpionsolitaire.game.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class Board {

    private static final Logger LOGGER = LoggerFactory.getLogger(Board.class);

    private final Set<Point> points;

    private final Set<Alignment> alignments;

    private final Mode gameMode;

    private int score = 0;

    private final String playerName;

    /**
     * Constructs a new board with a given mode.
     *
     * @param mode the game mode for this board
     * @param playerName the name of the player
     */
    public Board(Mode mode, String playerName) {
        if (mode == null)
            throw new NullPointerException("mode can't be null.");
        if (playerName == null)
            throw new NullPointerException("playerName can't be null.");
        if (playerName.isBlank() || playerName.isEmpty())
            throw new IllegalArgumentException("playerName can't be blank or empty.");
        this.points = new HashSet<>();
        this.alignments = new HashSet<>();
        this.gameMode = mode;
        this.playerName = playerName;
        LOGGER.info("Created a new {} board for {}.", mode.getId(), playerName);
    }

    /**
     * Initialize the board with the middle cross.
     */
    public void initialize() {
        final int x = 6;
        final int y = 3;
        final int width = 4;

        if(!gameMode.isSharpMode()){
            for(int i = 0; i < width; i++){
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
        } else {
            points.add(new Point(0, 8));
            points.add(new Point(1, 8));
            points.add(new Point(2, 8));
            points.add(new Point(3, 8));

            points.add(new Point(5, 8));
            points.add(new Point(6, 8));
            points.add(new Point(7, 8));

            points.add(new Point(9, 8));
            points.add(new Point(10, 8));
            points.add(new Point(11, 8));

            points.add(new Point(13, 8));
            points.add(new Point(14, 8));
            points.add(new Point(15, 8));

            points.add(new Point(1, 9));
            points.add(new Point(1, 7));

            points.add(new Point(2, 10));
            points.add(new Point(2, 9));
            points.add(new Point(2, 7));
            points.add(new Point(2, 6));

            points.add(new Point(3, 11));
            points.add(new Point(3, 10));
            points.add(new Point(3, 9));
            points.add(new Point(3, 7));
            points.add(new Point(3, 6));
            points.add(new Point(3, 5));

            points.add(new Point(4, 11));
            points.add(new Point(4, 10));
            points.add(new Point(4, 6));
            points.add(new Point(4, 5));
        }
        LOGGER.info("Board initialized.");
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
     * Adds a point to the board if the move is valid.
     *
     * @param point the point to be added
     * @return the set of all possible alignment with this point
     */
    public Set<Alignment> addPoint(Point point) {
        if (point == null)
            throw new NullPointerException("point can't be null");
        if (isValidMove(point)) {
            score++;
            points.add(new Point(point, score));
            LOGGER.info("{} added to the board.", point);
            return detectAlignments(point);
        }
        return new HashSet<>();
    }

    /**
     * Adds an alignment to the board.
     *
     * @param alignment the alignment to be added
     */
    public void addAlignment(Alignment alignment) {
        if (alignment == null)
            throw new NullPointerException("alignment can't be null.");
        alignments.add(new Alignment(alignment, score));
        LOGGER.info("{} added to the board.", alignment);
        LOGGER.info("Score is now {}.", score);
    }

    /**
     * Undo the last point added, by deleting it, deleting the associated alignment and decrementing the score.
     *
     * @return the deleted point
     */
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
        LOGGER.info("Undoing the last move");
        LOGGER.info("{} has been removed.", undoPoint);
        LOGGER.info("{} has been removed.", undoAlignmenet);
        LOGGER.info("Score is now {}.", score);
        return undoPoint;
    }

    /**
     * Checks if the given point move is valid.
     * A point that is not in the board of 16x16 is considered as invalid
     *
     * @param point the point to be checked
     * @return true if the move is valid, false otherwise
     */
    private boolean isValidMove(Point point) {
        if (point == null)
            throw new NullPointerException("point can't be null.");
        if (points.contains(point) || isAlignedPoint(point)) return false;
        if (point.getX() < 0 || point.getY() < 0 || point.getX() > 15 || point.getY() > 15) return false;
        Set<Alignment> detectedAlignments = detectAlignments(point);
        return !detectedAlignments.isEmpty();
    }

    /**
     * Detects an alignment for the given point.
     *
     * @param point the point for which alignment needs to be detected
     * @return a set of points that are aligned, null if none found
     */
    private Set<Alignment> detectAlignments(Point point) {
        if (point == null)
            throw new NullPointerException("point can't be null.");
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
        if (point == null)
            throw new NullPointerException("point can't be null.");
        if (direction == null)
            throw new NullPointerException("direction can't be null.");
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
                if (alignedPoints.size() == Alignment.ALIGNMENT_SIZE) {
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
     * Check if a point is contained in an alignment.
     *
     * @param point the point to check if it is aligned
     * @return true if the point is aligned, false otherwise
     */
    private boolean isAlignedPoint(Point point) {
        if (point == null)
            throw new NullPointerException("point can't be null.");
        for (Alignment alignment : alignments) {
            if (alignment.getPoints().contains(point))
                return true;
        }
        return false;
    }

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

    public String getPlayerName(){
        return playerName;
    }
}
