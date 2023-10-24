package fr.nathzaf.projects.morpionsolitaire;

public enum Mode {
    TOUCHING('T', 2), // 5T

    DISJOINT('D', 1); // 5D

    private final char id;

    private final int maxCommonPoints;

    Mode(char id, int maxCommonPoints){
        this.id = id;
        this.maxCommonPoints = maxCommonPoints;
    }

    public char getId(){
        return id;
    }

    public int getMaxCommonPoints(){
        return maxCommonPoints;
    }
}

