package fr.nathzaf.projects.morpionsolitaire.game;

public enum Mode {
    TOUCHING("5T", 2, false),

    TOUCHING_SHARP("5T#", 2, true),

    DISJOINT("5D", 1, false),

    DISJOINT_SHARP("5D#", 1, true);

    private final String id;

    private final int maxCommonPoints;

    private final boolean sharpMode;

    Mode(String id, int maxCommonPoints, boolean sharpMode) {
        this.id = id;
        this.maxCommonPoints = maxCommonPoints;
        this.sharpMode = sharpMode;
    }

    public String getId() {
        return id;
    }

    public int getMaxCommonPoints() {
        return maxCommonPoints;
    }

    public boolean isSharpMode() {
        return sharpMode;
    }
}

