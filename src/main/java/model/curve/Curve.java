package model.curve;

import java.io.Serializable;
import java.util.TreeSet;

public class Curve implements Serializable{
    /** auto generated serial Version */
    private static final long serialVersionUID = 6753830198895233150L;
    private String name;             // Curve name designation
    private TreeSet<Grade> curve;    // Set of grades that define this curve
    
    /**
     * Returns an initialized curve.
     */
    public Curve() {
        this.name = null;
        curve = new TreeSet<Grade>();
        curve.add(new Grade("A", 100, 90));
		curve.add(new Grade("B", 90, 80));
		curve.add(new Grade("C", 80, 70));
		curve.add(new Grade("D", 70, 60));
		curve.add(new Grade("F", 60, 0));
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
    
    /**
     * Returns the set of grades that defines this curve.
     * @return the set of grades that defines this curve
     */
    public TreeSet<Grade> getGrades() {
        return curve;
    }
    
    /**
     * Returns the grade that represents the percentage score.
     * @param percentage the percentage score
     * @return the grade that represents the percentage score
     */
    public Grade get(double percentage) {
        for (Grade grade : curve) {
            if (grade.contains(percentage)) {
                return grade;
            }
        }
        return null;
    }
}
