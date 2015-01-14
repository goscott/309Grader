package model.curve;

import java.util.TreeSet;

public class Curve {
    private String name;             // Curve name designation
    private TreeSet<Grade> curve;    // Set of grades that define this curve
    
    /**
     * Returns an empty curve.
     */
    public Curve() {
        curve = new TreeSet<Grade>();
    }
    
    /**
     * Returns an empty curve with the given name designation.
     * @param name the curve name designation
     */
    public Curve(String name) {
        this.name = name;
        curve = new TreeSet<Grade>();
    }

    /**
     * Add a unique grade to the curve. Grade percentage ranges must not
     * overlap with existing grades.
     * @param grade the grade to add
     */
    public void add(Grade grade) {
        curve.add(grade);
    }

    /**
     * Returns the curve name designation.
     * @return the curve name designation
     */
    public String name() {
        return name;
    }
    
    /**
     * Remove the given grade from this curve.
     * @param grade the grade to remove
     */
    public void remove(Grade grade) {
        curve.remove(grade);
    }

    /**
     * Reset the given grades percentage maximum and minimum.
     * @param grade the grade to reset
     * @param max the new maximum percentage
     * @param min the new minimum percentage
     */
    public void adjust(Grade grade, float max, float min) {
        grade.set(max, min);
    }
}
