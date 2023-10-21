package fr.nathzaf.projects.morpionsolitaire;

import com.google.common.collect.ImmutableSet;

import java.util.Objects;
import java.util.Set;

public class Alignment {

    private final ImmutableSet<Point> points;

    public Alignment(Set<Point> points){
        if(points.size() != 5) {
            throw new IllegalArgumentException("An alignment must be composed of 5 points.");
        }
        this.points = ImmutableSet.copyOf(points);
    }

    public ImmutableSet<Point> getPoints(){
        return points;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Alignment alignment = (Alignment) o;
        return Objects.equals(points, alignment.points);
    }

    @Override
    public int hashCode(){
        return Objects.hash(points);
    }
}
