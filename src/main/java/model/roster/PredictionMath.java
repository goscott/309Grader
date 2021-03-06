package model.roster;

import java.util.HashMap;

import model.curve.Grade;
import model.driver.Grader;

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
    public static HashMap<GradedItem, Double> getPrediction(Student stud, Grade desiredGrade) {
        double maxPoints, curPoints, reqPoints, availablePoints, chunk;
        int ungradedAssignments = 0;
        HashMap<GradedItem, Double> result = new HashMap<GradedItem, Double>();
//        String word = " are ";
        
        Roster roster = Grader.getRoster();
        Student student = Grader.getStudentList().get(Grader.getStudentList().indexOf(stud));
//        Debug.log("PredictionMath", "Desired grade for student " + student.getName() + ": " + desiredGrade.getName());
        
        //get max points for this roster
        maxPoints = roster.getMaxPoints();
        
        //get curent number of points this student has
        curPoints = student.getTotalScore();
//        Debug.log("PredictionMath", "Current Points: " + curPoints);
        //calculate required points
        // (cur + x) / tot = desired -----> x = (desired * tot) - cur 
        reqPoints = (desiredGrade.value() * DECIMAL_SHIFT * maxPoints) - curPoints;
//        Debug.log("PredictionMath", "Required Points: " + reqPoints);
        
        availablePoints = 0;
        
        //get the ungraded assignments
        for (GradedItem item : Grader.getAssignmentList()) {
            // not extra credit, and has no children
            if (!item.isExtraCredit() && item.isLeaf()) {
                if (Grader.getScore(student, item.name()) == null) {
                    ungradedAssignments++;
                    result.put(item, null);
                    availablePoints += item.maxScore();
                }
            }
        }
        //check to see if the grade is possible
        if (availablePoints < reqPoints) {
            return null;
        }
        
        //give a portion of the remaining points to each assignment 
        //chunk = Math.ceil(reqPoints / ungradedAssignments);
        chunk = (reqPoints / ungradedAssignments);
        
        //first try to give each gradeded item an equal chunk of the remaining points
        for (GradedItem item : result.keySet()) {
            //make sure we dont add too many points because of rounding
            if (reqPoints < chunk) {
                chunk = reqPoints;
            }
            //can fit a whole chunk
            if (chunk <= item.maxScore()) {
                result.put(item, chunk);
                reqPoints -= chunk;
            }
                //can only fit part of a chunk
            else {
                result.put(item, item.maxScore());
                reqPoints -= item.maxScore();
            }
        }
        
        //next add the rest of the points to any item available
        outerloop:
        for (GradedItem item : result.keySet()) {
            if (reqPoints == 0) {
                break outerloop;
            }
            chunk = item.maxScore() - result.get(item);
            if (chunk > reqPoints) {
                chunk = reqPoints;
            }
            result.put(item, result.get(item) + chunk);
            reqPoints -= chunk;
        }
        //return the assignments
        return result;
    }
}
