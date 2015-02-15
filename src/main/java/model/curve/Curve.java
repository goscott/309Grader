package model.curve;

import java.io.Serializable;
import java.util.TreeSet;

import model.driver.Debug;

/**
 * A representation of a course curve defined by percentage grades.
 * @author Frank Poole
 */
public class Curve implements Serializable{
    /** auto generated serial Version */
    private static final long serialVersionUID = 6753830198895233150L;
    /** Curve name designation */
    private String name;
    /** Set of grades that defines this curve */
    private TreeSet<Grade> curve;
    
    /**
     * Returns an initialized curve.
     */
    public Curve() {
        this.name = "";
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
    /*@
       requires !curve.contains(grade);
    @*/
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
     * @return false if not such grade is in the curve
     */
    /*@
       public normal_behavior
           requires curve.contains(grade);
           assignable \nothing;
           ensures !curve.contains(grade) && \result == true;
       also
       public normal_behavior
           assignable \nothing;
           ensures \result == false;
    @*/
    public boolean remove(Grade grade) {
        return curve.remove(grade);
    }

    /**
     * Reset the given grades percentage maximum and minimum.
     * @param grade the grade to reset
     * @param max the new maximum percentage
     * @param min the new minimum percentage
     */
    /*@
    public normal_behavior
        requires max <= 100.0 && min >= 0.0 && max > min;
    also
    public normal_behavior
        requires !(max <= 100.0 && min >= 0.0 && max > min);
    @*/
    public void adjust(Grade grade, float max, float min) {
        try {
            grade.set(max, min);
        }
        catch (IllegalArgumentException e) {
            Debug.log("Grade Not Adjusted: Invalid Grade Range Argument(s)");
        }
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
    /*@
       requires \forall grade != null;
     @*/
    public Grade get(double percentage) {
        for (Grade grade : curve) {
            if (grade.contains(percentage)) {
                return grade;
            }
        }
        if(percentage <= 0) {
        	return curve.first();
        }
        if(percentage >= 100) {
        	return curve.last();
        }
        return null;
    }
}
