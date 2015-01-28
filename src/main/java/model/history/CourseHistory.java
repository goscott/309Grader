package model.history;
import java.util.*;

import model.roster.Roster;

/**
 * Class that holds a record of a sections taught of a Single course.
 *
 * @author Mason Stevenson
 */
public class CourseHistory {
	public ArrayList<Roster> history;
	public ArrayList<Roster> hidden;
	public int totalStudents;
	public int averageGrade;
	public int startYear;
	public int endYear;
	public int numSectionsTaught;
	private String courseName;
	
	/**
	 * Constructor for history.
	 * @param newCourseName The name of a course.
	 */
	public CourseHistory(String newCourseName) {
	    courseName = newCourseName;
	}
	
	/**
	 * Adds a roster to the history.
	 * @param newRoster A roster to add.
	 */
	public void addRoster(Roster newRoster) {
	    history.add(newRoster);
	}
	
	/**
	 * @return returns the course history.
	 */
	public Collection<Roster> getHistory () {
		return history;
	}
	
	/**
	 * Adds a course to the history.
	 * @param newCourse the course to add.
	 */
	public void removeCourse(Roster targetCourse) {
		if (history.contains(targetCourse)) {
		    history.remove(targetCourse);
		}
	}
	
	/**
	 * Hides a course in the history.
	 * @param targetCourse the course to hide.
	 */
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
	public void unHideCourse(Roster targetCourse) {
	    if (hidden.contains(targetCourse)) {
	        history.add(targetCourse);
            hidden.remove(targetCourse);
	    }
	}
	
	/**
	 * @return returns the total number of students who have taken this course.
	 */
	public int getTotalStudents() {
		return 0;
	}
	
	/**
	 * @return returns the average grade of all students in this course.
	 */
	public int getAverageGrade() {
		return 0;
	}
	
	/**
	 * @return returns the first year this course was taught.
	 */
	public int getStartYear() {
        	return 0;
	}
	
	/**
	 * @return returns the last year this course was taught.
	 */
	public int getEndYear() {
        	return 0;
	}
	
	/**
	 * @return returns the total number of sections taught for this course.
	 */
	public int getNumSectionsTaught() {
        	return 0;
	}
	
	/**
	 * Returns the name for this course.
	 * @return The course name. Example: "CSC/CPE 309"
	 */
	public String getCourseName() {
	    return courseName;
	}
}
