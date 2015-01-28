package model.curve;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;

/**
 * A representation of a percentage based grade.
 * @author Frank Poole
 */
public class Grade implements Comparable<Grade>, Serializable {
    /**
     * auto Generated serial ID
     */
    private static final long serialVersionUID = -8509411443331361582L;
    private final SimpleStringProperty name;    // Grade Name
    private double max;      // Maximum percentage required
    private double min;      // Minimum percentage required

    /**
     * Create a new Grade object with
     * @param name the grade name designation
     * @param max the maximum percentage required
     * @param min the minimum percentage required
     */
    public Grade(String name, float max, float min) {
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
    
    public boolean overlap(Grade other) {
        return this.min <= other.max && other.min <= this.max;
    }
    
    public boolean contains(double percentage) {
        return percentage >= this.min && percentage <= this.max;
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
    public void set(double max, double min) {
        if (max <= 100 && min >= 0 && max >= min) {
            this.max = max;
            this.min = min;
        }
        else {
            throw new IllegalArgumentException(
                "Invalid grade percentage range");
        }
    }
}