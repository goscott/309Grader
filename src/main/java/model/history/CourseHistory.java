package model.history;
import java.io.Serializable;
import java.util.*;

import model.driver.Debug;
import model.roster.Roster;
import model.roster.Student;

/**
 * Class that holds a record of a sections taught of a Single course.
 *
 * @author Mason Stevenson
 */
public class CourseHistory implements Serializable {
    
    private static final long serialVersionUID = 6886136489500479622L;

    /**
     * All the rosters (different sections from different years) for this course.
     */
	private ArrayList<Roster> history = new ArrayList<Roster>();
	
	/**
	 * Hidden rosters go here.
	 */
	private ArrayList<Roster> hidden = new ArrayList<Roster>();
	
	/**
	 * Keeps track of how many different quarters there are.
	 */
	private ArrayList<String> quarters = new ArrayList<String>();
	
	/**
	 * The sum of all students who have ever taken this course.
	 */
	private int totalStudents;
	
	/**
	 * The average grade of all students who have taken this course.
	 */
	private double averageGrade;
	
	/**
	 * The first year this course was available.
	 */
	private int startYear;
	
	/**
	 * The most recent year this course was available.
	 */
	private int endYear;
	
	/**
	 * The total number of sections taught for this course.
	 */
	private int numSectionsTaught;
	
	/**
	 * The name of this course.
	 */
	private String courseName;
	
	/**
	 * The sum of all final grades in all rosters.
	 */
	private double totalGradeValue;
	
	/**
	 * Constructor for history.
	 * @param newCourseName The name of a course.
	 */
	public CourseHistory(String newCourseName) {
	    courseName = newCourseName;
	    startYear = -1;
	    endYear = -1;
	    numSectionsTaught = 0;
	}
	
	/**
	 * Adds a roster to the history.
	 * @param newRoster A roster to add.
	 */
	/*@
    //The course does not already exist in history
        requires
            ( !(\exists Roster roster_other ;
                history.contains(roster_other) ;
                    course_other.equals(newRoster)));
    
        //only the course was added to history
        ensures
            (\forall Roster roster_other ;
                history.contains(roster_other) <==>
                    course_other.equals(newRoster) || \old(history).contains(roster_other));
    @*/
	public void addRoster(Roster newRoster) {
	    
	    for (Roster rost : history) {
	        if (newRoster.getSection() == rost.getSection() && newRoster.getQuarter() == newRoster.getQuarter()) {
	            Debug.log("History- Error", "Tried to add a course that is already in the history");
	            return;
	        }
	    }
	    
	    for (Roster rost : hidden) {
	        if (newRoster.getSection() == rost.getSection() && newRoster.getQuarter() == newRoster.getQuarter()) {
	            Debug.log("History- Error", "Tried to add a course that is already in the history");
                return;
            }
	    }
	    //check newRoster's year to see if we need to update start year/end year
	    
	    //add students to total students
	    totalStudents += newRoster.getStudents().size();
	    history.add(newRoster);
	    
	    numSectionsTaught++;
	    
	    if (!quarters.contains(newRoster.getQuarter())) {
	        quarters.add(newRoster.getQuarter());
	    }
	}
	
	/**
	 * @return returns the course history.
	 */
	/*@
        ensures (
            (\result == history)
        );
    @*/
	public Collection<Roster> getHistory () {
		return history;
	}
	
	/**
	 * Adds a course to the history.
	 * @param newCourse the course to add.
	 */
	/*@
	 
        requires
        //the course must be in the history
            (history.contains(targetCourse));
        
        ensures
        
        //history contains only courses that were in the old version and are not the target
            (\forall Roster roster_other ;
                history.contains(roster_other) <==> 
                    !course_other.equals(targetCourse) && \old(history).contains(roster_other)
    
            && 
            
        //nothing else was deleted
            \forall Roster roster_other2 ;
                \old(history).contains(roster_other2) ==>
                    history.contains(roster_other2) || course_other2.equals(targetCourse));
    @*/
	public void removeCourse(Roster targetCourse) {
		if (history.contains(targetCourse)) {
		    history.remove(targetCourse);
		}
	}
	
	/**
	 * Hides a course in the history.
	 * @param targetCourse the course to hide.
	 */
	/*@
        requires
        
        //the course is in the history
            (history.contains(targetCourse));
    
        ensures
        
        //the course has been added to hidden
            (hidden.contains(targetCourse)
    
            &&
    
        //the course has been removed from history
            !history.contains(targetCourse)
    
            &&
    
        //hidden contains only the same courses as before or target coruse
            \forall Roster course_other1;
                hidden.contains(course_other1) <==> 
                    course_other1.equals(targetCourse) || \old(hidden).contains(course_other1)
            &&
            
        //history only contains the same courses as before - targetCourse
            \forall Roster course_other2 ;
                history.contains(course_other2) <==>
                    !course_other2.equals(targetCourse) && \old(history).contains(course_other2)
    
            &&
    
        //nothing else has changed in history
            \forall Roster course_other3 ; 
                \old(history).contains(course_other3) ==>
                    history.contains(course_other3));

    @*/
	public void hideCourse(Roster targetCourse) {
		if (history.contains(targetCourse)) {
		    hidden.add(targetCourse);
		    history.remove(targetCourse);
		}
	}

	/**
 	 * Un-hides a course in the history
 	 * @param targetCourse the course to unHide
 	 */
	/*@
        ensures (
        
            //the course is back in history, and not in hidden, and nothing else
            //has changed in either collection
    
            //the course has been added to history
            history.contains(targetCourse)
    
            &&
    
            //the course has been removed from hidden
            !hidden.contains(targetCourse)
            
            &&
    
            \forall Roster course_other1 ;
                history.contains(course_other1) <==>
                    course_other1.equals(targetCourse) || \old(history).contains(course_other1)
            &&
            
            \forall Roster course_other2 ;
                hidden.contains(course_other2) <==>
                    !course_other2.equals(targetCourse) && \old(hidden).contains(course_other2)
    
            &&
    
            \forall Roster course_other3 ;
                \old(hidden).contains(course_other3) ==>
                    hidden.contains(course_other3) || course_other3.equals(targetCourse)
            );
    @*/
	public void unHideCourse(Roster targetCourse) {
	    if (hidden.contains(targetCourse)) {
	        history.add(targetCourse);
            hidden.remove(targetCourse);
	    }
	}
	
	/**
	 * @return returns the total number of students who have taken this course.
	 */
	/*@
        //gets the correct result
        ensures
            (\result == totalStudents);
    @*/
	public int getTotalStudents() {
		return totalStudents;
	}
	
	/**
	 * @return returns the average grade of all students in this course.
	 */
	/*@
        //gets the correct result
        ensures
            (\result == averageGrade);
    @*/ 
	public double getAverageGrade() {
	    double sum = 0;
        
        for (Roster roster : history) {
            sum += roster.getPercentAverage();
        }
        
        for (Roster roster : hidden) {
            sum += roster.getPercentAverage();
        }
        
        if (numSectionsTaught > 0) {
            return sum / numSectionsTaught;
        }
        
        else {
            return 0;
        }
		
		
	}
	
	/**
	 * Gets the average grade for a single quarter.
	 */
	public double getAverageGrade(int year, String quarter) {
	    return 0;
	}
	
	/**
	 * Returns a list of all averages.
	 */
	public ArrayList<Double> getAverages() {
	    double sum;
	    double count;
	    
	    ArrayList<Double> list = new ArrayList<Double>();
	    
	    for (String quarter : quarters) {
	        sum = 0;
	        count = 0;
	        
	        for (Roster roster : history) {
	            if (roster.getQuarter().equals(quarter)) {
	                count++;
	                sum += roster.getPercentAverage();
	            }
	        }
	        
	        if (count > 0) {
	            list.add(sum / count);
	        }
	    }
	    
	    /*
	    for (Roster roster : history) {
	        list.add(roster.getPercentAverage());
	        Debug.log("HISTORY", "Added value " + roster.getPercentAverage() + " to graph");
	    }*/
	    
	    return list;
	}
	
	public ArrayList<Double> getAveragesDumb() {
	    ArrayList<Double> list = new ArrayList<Double>();
	    list.add(60 + (Math.random()* (100 - 60 + 1)));
	    list.add(60 + (Math.random()* (100 - 60 + 1)));
	    list.add(60 + (Math.random()* (100 - 60 + 1)));
	    list.add(60 + (Math.random()* (100 - 60 + 1)));
	    list.add(60 + (Math.random()* (100 - 60 + 1)));
	    
	    return list;
	}
	
	/**
	 * @return returns the first year this course was taught.
	 */
	/*@
        //gets the correct result
        ensures
            (\result == startYear);
    @*/
	public int getStartYear() {
        	return startYear;
	}
	
	/**
	 * @return returns the last year this course was taught.
	 */
	/*@
        //gets the correct result
        ensures
            (\result == endYear);
    @*/
	public int getEndYear() {
	    return endYear;
	}
	
	/**
	 * @return returns the total number of sections taught for this course.
	 */
	/*@
        //gets the correct result
        ensures
            (\result == numSectionsTaught);
    @*/
	public int getNumSectionsTaught() {
        	return numSectionsTaught;
	}
	
	/**
	 * Returns the name for this course.
	 * @return The course name. Example: "CSC/CPE 309"
	 */
	/*@
        //gets the correct result
        ensures
            (\result == courseName);
    @*/
	public String getCourseName() {
	    return courseName;
	}
	
	public double getAverageClassSize() {
	    double sum = 0;
	    
	    for (Roster roster : history) {
	        sum += roster.getStudents().size();
	    }
	    
	    for (Roster roster : hidden) {
	        sum += roster.getStudents().size();
	    }
	    
	    if (numSectionsTaught > 0) {
	        return sum / numSectionsTaught;
	    }
	    
	    else {
	        return 0;
	    }
	}
}
