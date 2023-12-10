package fr.nathzaf.projects.morpionsolitaire.game;

public enum Mode {
    TOUCHING("5T", 2), // 5T

    DISJOINT("5D", 1); // 5D

    private final String id;

    private final int maxCommonPoints;

    Mode(String id, int maxCommonPoints) {
        this.id = id;
        this.maxCommonPoints = maxCommonPoints;
    }

    public String getId() {
        return id;
    }

    public int getMaxCommonPoints() {
        return maxCommonPoints;
    }
}

