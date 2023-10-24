package fr.nathzaf.projects.morpionsolitaire;

public enum Direction {
    HORIZONTAL(1, 0),
    VERTICAL(0, 1),
    DIAGONAL_BOTTOM(1, 1),
    DIAGONAL_TOP(1, -1);

    private final int dx;

    private final int dy;

    Direction(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx(){
        return dx;
    }

    public int getDy(){
        return dy;
    }
}