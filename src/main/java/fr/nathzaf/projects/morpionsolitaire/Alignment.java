package fr.nathzaf.projects.morpionsolitaire;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class Alignment {

    private final ImmutableSet<Point> points;

    public Alignment(Set<Point> points){
        this.points = ImmutableSet.copyOf(points);
    }

    public ImmutableSet<Point> getPoints(){
        return points;
    }

}
