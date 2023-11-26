package fr.nathzaf.projects.morpionsolitaire.components;

public class Point {
    private final int x;
    private final int y;
    private final int score;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        score = -1;
    }

    public Point(Point point, int score) {
        this.x = point.getX();
        this.y = point.getY();
        this.score = score;
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
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
