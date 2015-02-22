package model.history;

import java.util.ArrayList;
import java.util.Arrays;

public class QuarterAverage implements Comparable {
    private final double val;
    private final String quarter;
    private final int year;
    
    public QuarterAverage(String newQuarter, int newYear, double newVal) {
        val = newVal;
        quarter = newQuarter;
        year = newYear;
    }
    
    public double getValue() {
        return val;
    }
    
    public String getQuarter() {
        return quarter;
    }
    
    public int getYear() {
        return year;
    }
    
    public int compareTo(Object other) {
        QuarterAverage otherAverage = (QuarterAverage) other;
        ArrayList<String> quarters = new ArrayList<String>(Arrays.asList(new String[]{"Fall", "Spring", "Winter", "Summer"}));
        
        if (otherAverage.getYear() != year) {
            return year - otherAverage.getYear();
        }
        
        else {
            return quarters.indexOf(quarter) - quarters.indexOf(otherAverage.getQuarter());
        }
    }
}
