package fr.nathzaf.projects.morpionsolitaire.components;

public class Point {

    private final int x;

    private final int y;

    private final int score;

    /**
     * Constructor for 'default' point, especially for initial cross and playable tests.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        score = -1;
    }

    /**
     * Constructor for 'played' point.
     *
     * @param point the point to be copied
     * @param score the score associated
     */
    public Point(Point point, int score) {
        if (point == null)
            throw new NullPointerException("point can't be null.");
        this.x = point.getX();
        this.y = point.getY();
        this.score = score;
    }

    public String generateCircleId() {
        return "#x" + x + "y" + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(x);
        result = 31 * result + Integer.hashCode(y);
        return result;
    }

    @Override
    public String toString(){
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", score=" + score +
                '}';
    }
}
