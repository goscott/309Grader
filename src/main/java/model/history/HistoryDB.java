package model.history;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import model.driver.Debug;
import model.roster.Roster;

/**
 * Holds multiple CourseHistory objects.
 * @author Mason Stevenson
 *
 */
public class HistoryDB implements Serializable {
    private static final long serialVersionUID = -3742583622867493094L;
    /** Master list of all courses in the history. */
    ArrayList<CourseHistory> history = new ArrayList<CourseHistory>();
    
    /**
     * Adds a roster to the history db.
     * @param newRoster A roster to add.
     */
    public void addRoster(Roster newRoster) {
        
        //check to see if the CourseHistory exists in the db
        if (!hasCourse(newRoster.courseName())) {
            history.add(new CourseHistory(newRoster.courseName()));
        }
        
        //add the roster to the course history
        getCourseHistory(newRoster.courseName()).addRoster(newRoster);
        save();
    }
    
    /**
     * Checks the HistoryDB to see if a coursehistory exists given a course name.
     * @param courseName The target course.
     * @return Returns true if the course is in the HistoryDB
     */
    public boolean hasCourse(String courseName) {
        
        for (CourseHistory target : history) {
            if (target.getCourseName().equals(courseName)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets a CourseHistory object given a course name.
     * @param courseName The target course.
     * @return Returns a CourseHistory object if it exists in the HistoryDB. Otherwise, returns null.
     */
    public CourseHistory getCourseHistory(String courseName) {
        
        for (CourseHistory target : history) {
            System.out.println("Checking " + target.getCourseName() + " against" + courseName);
            if (target.getCourseName().equals(courseName)) {
                return target;
            }
        }
        
        return null;
    }
    
    /**
     * Returns all course history objects.
     */
    public ArrayList<CourseHistory> getHistory() {
        return history;
    }
    
    public boolean save() {
        File direc = new File("History");
        
        if (!direc.exists()) {
            direc.mkdir();
        }
        
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("History/data.hdb"));
            out.writeObject(this);
            out.close();
            return true;
        } 
        
        catch (IOException ex) {
            Debug.log("SAVE ERROR", "failed to save History to  History/data.hdb");
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Returns a string representation of this historyDB
     */
    public String toString() {
        String message = "\n--------------------------History--------------------------\n\n";
        for (CourseHistory targetHistory : history) {
            
            message += "COURSENAME: " + targetHistory.getCourseName() + "\n";
            for (Roster targetRoster : targetHistory.getHistory()) {
                message += "        " + targetRoster.getQuarter() + " " + 2015 + " Section" + targetRoster.getSection() + "\n"; //FIX YEAR
            }
            
            message += "\n";
        }
        message += "-----------------------------------------------------------\n";
        return message;
    }
}
