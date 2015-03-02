package model.curve;

import java.awt.Color;
import java.io.Serializable;

/**
 * A representation of a percentage based grade.
 * @author Frank Poole
 */
public class Grade implements Comparable<Grade>, Serializable {
    /** auto Generated serial ID */
    private static final long serialVersionUID = -8509411443331361582L;
    /** Grade Name */
    private final String name;
    /** Grades above this value have this grade */
    private double value;
    /** The color of the grade **/
    private Color color;

    /**
     * Create a new Grade object with
     * @param name the grade name designation
     * @param max the maximum percentage required
     * @param min the minimum percentage required
     */
    public Grade(String name, double val, Color color) {
        this.name = name;
        this.color = color;
        this.set(val);
    }
    
    /**
     * Gets the color of the grade
     */
    public Color getColor() {
    	return color;
    }

    /**
     * Returns a negative integer, zero, or a positive integer as this grade is
     * less than, equal to, or greater than the specified grade. Overlapping
     * grades are considered duplicates and therefore equivalent.
     * @return a negative integer, zero, or a positive integer as this grade is
     * less than, equal to, or greater than the specified grade.
     */
    @Override
    public int compareTo(Grade other) {
    	return Double.compare(value, other.value);
    }
    
    /**
     * Returns true if this grade is equivalent to another grade as indicated
     * by its name.
     * @param obj the object being compared to this grade
     * @return true if the object is equivalent
     */
    @Override
    public boolean equals(Object obj) {
        if (name != null && obj != null && (obj instanceof Grade))
        {
            return name.equals(((Grade) obj).getName());
        }
        
        return false;
    }
    
    /**
     * Return true if the given percentage score lies within this grade's range.
     * @param percentage the percentage score
     * @return true if percentage score is in range
     */
    /*@
    public normal_behavior
        requires percentage <= 100.0 && percentage >= 0.0 && 
            percentage >= min && percentage < max;
        assignable \nothing;
        ensures \result == true;
    also
    public normal_behavior
        requires !(percentage <= 100.0 && percentage >= 0.0 && 
            percentage >= min && percentage < max);
        assignable \nothing;
        ensures \result == false;
    @*/
    public boolean contains(double percentage) {
    	return percentage >= value;
    }
    
    /**
     * Returns the grade name designation.
     * @return the grade name designation
     */
    /*@
        ensures \result.equals(name);
    @*/
    public String getName() {
        return name;
    }
    
    /**
     * Returns the value
     */
    /*@
        ensures \result.equals(min);
    @*/
    public double value() {
        return value;
    }
    
    /**
     * Set the maximum and minimum percentage required.
     * @param max the new maximum percentage required
     * @param min the new minimum percentage required
     */
    /*@
       public normal_behavior
           requires max <= 100.0 && min >= 0.0 && max > min;
           assignable max;
           assignable min;
           ensures max == max;
           ensures min == min;
       also
       public exceptional_behavior
           requires !(max <= 100.0 && min >= 0.0 && max > min);
           signals_only IllegalArgumentException;
    @*/
    public void set(double val) {
        if (val <= 100.0 && val >= 0.0) {
            value = (int)val;
        }
        else {
            throw new IllegalArgumentException(
                "Invalid grade percentage");
        }
    }
    
    /**
     * Overrides the toString method to retun the name
     */
    public String toString() {
    	return name;
    }

}