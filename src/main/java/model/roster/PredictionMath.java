package model.roster;

import java.util.ArrayList;

import model.curve.Grade;
import model.driver.Debug;

/**
 * Handles grade prediction calculations.
 * @author Mason Stevenson
 */
public class PredictionMath {
    /**
     * Makes a whole number percent grade into a decimal percent grade. e.g. 67 becomes .67
     */
    private static final double DECIMAL_SHIFT = .01;
    
    /**
     * Given a student, a roster, and a desired grade, returns a list of assignment grades needed for that
     * student to achieve the desired grade.
     */
    public static ArrayList<GradedItem> getPrediction(Roster roster, Student student, Grade desiredGrade) {
        double maxPoints, curPoints, reqPoints, availablePoints;
        int ungradedAssignments = 0;
        GradedItem temp;
        ArrayList<GradedItem> result = new ArrayList<GradedItem>();
        String word = " are ";
        
        Debug.log("PredictionMath", "Desired grade for student " + student.getName() + ": " + desiredGrade.getName());
        
        //get max points for this roster
        maxPoints = roster.getMaxPoints();
        
        //get curent number of points this student has
        curPoints = student.getTotalScore();
        
        //calculate required points
        // (cur + x) / tot = desired -----> x = (desired * tot) - cur 
        reqPoints = (desiredGrade.value() * DECIMAL_SHIFT * maxPoints) - curPoints;
        Debug.log("PredictionMath", "Required Points: " + reqPoints);
        
        availablePoints = 0;
        
        //get the ungraded assignments
        for (GradedItem item : roster.getAssignments()) {
            
            // not extra credit, and has no children
            if (!item.isExtraCredit() && item.hasChildren()) {
                if (item.getStudentScore(student) == null) {
                    ungradedAssignments++;
                    result.add(item.copy());
                    availablePoints += item.maxScore();
                }
            }
        }
        
        if (ungradedAssignments == 1) {
            word = " is ";
        }
        
        Debug.log("PredictionMath", "There " + word + ungradedAssignments + " ungraded assignment(s) for this student, with " + availablePoints + " available points");
        
        //check to see if the grade is possible
        if (availablePoints < reqPoints) {
            Debug.log("PredictionMath", "This grade is not possible");
            return null;
        }
        
        //give a portion of the remaining points to each assignment 
        
        //return the assignments
        return result;
    }
}
