package fr.nathzaf.projects.morpionsolitaire;

public enum Mode {
    TOUCHING('T'), // 5T

    DISJOINT('D'), // 5D

    RANDOM_SOLVER('R');

    private final char id;

    Mode(char id){
        this.id = id;
    }

    public char getId(){
        return id;
    }
}

