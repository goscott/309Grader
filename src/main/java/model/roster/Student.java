package model.roster;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import model.administration.PermissionKeys;
import model.administration.User;
import model.administration.UserTypes;
import model.curve.Grade;
import model.driver.Debug;
import model.driver.Grader;

/**
 * A student in a class
 * 
 * @author Gavin Scott
 */
public class Student implements Comparable<Student>, Serializable {
	/** Generated serial ID **/
	private static final long serialVersionUID = 6298208303690715171L;
	/** The student's name **/
	private final String name;
	/** The student's id **/
	private final String id;
	/** The roster for this student **/
	private Roster roster;

	/**
	 * Makes a student without a roster
	 */
	public Student(String name, String id) {
		this(name, id, null);
	}

	/**
	 * Creates a student with the given information
	 */
	public Student(String name, String id, Roster roster) {
		this.roster = roster;
		this.name = name;
		this.id = id;
	}

	/**
	 * Enrolls a student in a course
	 */
	public void setRoster(Roster roster) {
		if (this.roster == null && roster != null) {
			this.roster = roster;
		}
	}

	/**
	 * Gets the student's name
	 * 
	 * @return String the name of the student
	 */
	public String getName() {
		User currentUser = Grader.getUser();
		if (currentUser != null
				&& currentUser.getPermissions().contains(PermissionKeys.VIEW_STUDENTS)
				&& !currentUser.getId().equals(id)) {
			return "*******";
		}
		return name;
	}

	/**
	 * Gets the student's ID
	 * 
	 * @return String the student's ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the student's total number of points
	 * 
	 * @return double the total grade
	 */
	public double getTotalScore() {
		if (roster != null) {
			return roster.getTotalScore(this);
		}
		return 0;
	}

	/**
	 * Gets the student's grade, represented by a percentage of possible points
	 * 
	 * @return double the percentage of total points scored out of total points
	 *         possible
	 */
	public double getTotalPercentage() {
		if (roster != null) {
			double maxTotal = roster.getMaxPoints();
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			return maxTotal > 0 ? Double.valueOf(twoDForm
					.format(getTotalScore() / maxTotal * 100)) : 0;
		}
		return 0;
	}

	/**
	 * Gets the students total grade as a Grade object, based on the classes
	 * current curve
	 * 
	 * @return Grade the student's total Grade
	 */
	public Grade getGrade() {
		double percent = getTotalPercentage();
		Debug.log("Student Grade", percent + "%");
		if (roster != null) {
			return roster.getCurve().get(percent);
		}
		return null;
	}

	/**
	 * Gets the student's grade on a particular assignment
	 * 
	 * @param asgn
	 *            the assignment
	 * @return Grade the grade
	 */
	public Grade getGrade(String asgn) {
		if (roster != null) {
			return roster.getCurve().get(getAssignmentScore(asgn));
		}
		return null;
	}

	/**
	 * Gets the student's score on an individual assignment
	 * 
	 * @param asgn
	 *            The name of the assignment
	 * @return Double the student's score on the assignment
	 */
	public Double getAssignmentScore(String asgn) {
		if (roster != null) {
			return roster.getStudentGrade(this, asgn);
		}
		return null;
	}

	/**
	 * Sets the score for an assignment
	 * 
	 * @param asgn
	 *            The name of the assignment
	 * @param sc
	 *            the new score
	 */
	public void setScore(String asgn, Double sc) {
		if (roster != null) {
			roster.setStudentGrade(this, asgn, sc);
		}
	}

	/**
	 * Sets the assignment's score to a percentage of the assignment's maximum
	 * score
	 * 
	 * @param asgn
	 *            the name of the assignment
	 * @param percent
	 *            the percent (90.0, etc)
	 */
	public void setPercentScore(String asgn, double percent) {
		if (roster != null) {
			setScore(asgn, percent / 100
					* roster.getAssignment(asgn).maxScore());
		}
	}

	/**
	 * Compares two students by their names
	 */
	public int compareTo(Student other) {
		return name.toString().compareTo(other.name.toString());
	}

	/**
	 * Checks two students for logical equality
	 * 
	 * @param other
	 *            The other object
	 * @return boolean true if logically equal
	 */
	public boolean equals(Object other) {
		if ((other != null) && (other instanceof Student)) {
			Student oth = (Student) other;
			return oth.getName().equals(name) && oth.getId().equals(id);
		}
		return false;
	}

	/**
	 * Saves a list of students
	 * 
	 * @param students
	 *            the list
	 * @return String a representation of the list of students
	 */
	public static String Save(ArrayList<Student> students) {
		String toReturn = "";
		char secret = 1;
		for (Student stu : students) {
			toReturn += "S" + secret;
			toReturn += stu.name.toString() + secret + stu.id + secret;
			toReturn += "\n";
		}

		return toReturn;
	}
}
