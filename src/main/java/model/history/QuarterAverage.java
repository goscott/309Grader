package model.history;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Holds the average course grade for a quarter/year.
 * @author Mason Stevenson
 */
public class QuarterAverage implements Comparable<QuarterAverage> {
    
    /**
     * The average of all student grades in a course.
     */
    private final double val;
    
    /**
     * The quarter this course was taught.
     */
    private final String quarter;
    
    /**
     * The year the course was taught.
     */
    private final int year;
    
    /**
     * Constructor for quarter average.
     */
    /*@
          ensures
              (
                  val == newVal &&
                  quarter.equals(newQuarter) &&
                  year == newYear
              );
     @*/
    public QuarterAverage(String newQuarter, int newYear, double newVal) {
        val = newVal;
        quarter = newQuarter;
        year = newYear;
    }
    
    /*@
          ensures
              (\result == val);
     @*/
    public double getValue() {
        return val;
    }
    
    /*@
        ensures
            (\result.equals(quarter));
    @*/
    public String getQuarter() {
        return quarter;
    }
    
    /*@
        ensures
            (\result == year);
    @*/
    public int getYear() {
        return year;
    }
    
    /**
     * Compares the QuarterAverages chronologically. 
     * Eg: Spring 2012 < Spring 2013 and Summer 2015 < Winter 2015
     */
    /*@
           requires
                 otherAverage != null;
             
            ensures
                (
                    otherAverage.getYear() != year ==> \result == year - otherAverage.getYear()
                
                    &&
                
                    otherAverage.getYear() == year ==> 
                        \result == (new ArrayList<String>(Arrays.asList(new String[]{"Spring", "Summer", "Fall", "Winter"}))).indexOf(quarter) 
                            - (new ArrayList<String>(Arrays.asList(new String[]{"Spring", "Summer", "Fall", "Winter"}))).indexOf(otherAverage.getQuarter())
                 );
     @*/
    public int compareTo(QuarterAverage otherAverage) {
        ArrayList<String> quarters = new ArrayList<String>(Arrays.asList(new String[]{"Spring", "Summer", "Fall", "Winter"}));
        
        if (otherAverage.getYear() != year) {
            return year - otherAverage.getYear();
        }
        
        else {
            return quarters.indexOf(quarter) - quarters.indexOf(otherAverage.getQuarter());
        }
    }
}
