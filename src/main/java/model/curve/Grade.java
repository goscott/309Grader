package model.curve;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;

/**
 * A representation of a percentage based grade.
 * @author Frank Poole
 */
public class Grade implements Comparable<Grade>, Serializable {
    /** auto Generated serial ID */
    private static final long serialVersionUID = -8509411443331361582L;
    /** Grade Name */
    private final SimpleStringProperty name;
    /** Maximum percentage required */
    private double max;
    /** Minimum percentage required */
    private double min;

    /**
     * Create a new Grade object with
     * @param name the grade name designation
     * @param max the maximum percentage required
     * @param min the minimum percentage required
     */
    public Grade(String name, double max, double min) {
        this.name = new SimpleStringProperty(name);
        this.set(max, min);
    }

    /**
     * Returns a negative integer, zero, or a positive integer as this grade is
     * less than, equal to, or greater than the specified grade. Overlapping
     * grades are considered duplicates and therefore equivalent.
     * @return a negative integer, zero, or a positive integer as this grade is
     * less than, equal to, or greater than the specified grade.
     */
    public int compareTo(Grade other) {
        if (this.overlap(other)) {
            return 0;
        }
        else {
            return Double.compare(this.max, other.max);
        }
    }
    
    /**
     * Returns true if the two grades percentage scores overlap ranges.
     * @param other another grade to compare with
     * @return true if the grades percentage scores overlap
     */
    public boolean overlap(Grade other) {
        return this.min < other.max && other.min < this.max;
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
        return percentage >= this.min && percentage < this.max;
    }
    
    /**
     * Returns the grade name designation.
     * @return the grade name designation
     */
    public String getName() {
        return name.get();
    }
    
    /**
     * Returns the maximum percentage required.
     * @return the maximum percentage required
     */
    public double max() {
        return max;
    }
    
    /**
     * Returns the minimum percentage required.
     * @return the minimum percentage required
     */
    public double min() {
        return min;
    }
    
    /**
     * Returns the range of the percentage required.
     * @return the range of the percentage
     */
    public double range() {
        return max - min;
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
    public void set(double max, double min) {
        if (max <= 100.0 && min >= 0.0 && max > min) {
            this.max = max;
            this.min = min;
        }
        else {
            throw new IllegalArgumentException(
                "Invalid grade percentage range");
        }
    }
}
