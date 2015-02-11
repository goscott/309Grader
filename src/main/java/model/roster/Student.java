package model.roster;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import model.curve.Grade;
import model.driver.Debug;
import model.driver.Grader;

/**
 * A student in a class
 * 
 * @author Gavin Scott
 */
public class Student implements Comparable<Student>, Serializable {
	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = 6298208303690715171L;
	/** The student's name **/
	private final String name;
	/** The student's id **/
	private final String id;
	/** A map of assignment names to the assignments in the class **/
	private HashMap<String, GradedItem> scores;

	/**
	 * Creates a student with the given information
	 * 
	 * @param name
	 *            The student's name
	 * @param id
	 *            The student's ID
	 */
	public Student(String name, String id) {
		this.name = name;
		this.id = id;
		scores = new HashMap<String, GradedItem>();
	}

	/**
	 * Gets the student's name
	 * 
	 * @return String the name of the student
	 */
	public String getName() {
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
		double total = 0;
		for (GradedItem item : scores.values()) {
			total += item.score() != null ? item.score() : 0;
		}
		return total;
	}

	/**
	 * Gets the student's grade, represented by a percentage of possible points
	 * 
	 * @return double the percentage of total points scored out of total points
	 *         possible
	 */
	public double getTotalPercentage() {
		double maxTotal = Grader.getMaxPoints();
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return maxTotal > 0 ? Double.valueOf(twoDForm.format(getTotalScore()
				/ maxTotal * 100)) : 0;
	}

	/**
	 * Gets the students total grade as a Grade object, based on the classes
	 * current curve
	 * 
	 * @return Grade the student's total Grade
	 */
	public Grade getGrade() {
		double percent = getTotalPercentage();
		Grade studentGrade = null;
		for (Grade grade : Grader.getCurve().getGrades()) {
			if (grade.min() <= percent && grade.max() >= percent) {
				studentGrade = grade;
			}
		}

		return studentGrade;
	}

	/**
	 * Gets the student's grade on a particular assignment
	 * 
	 * @param asgn
	 *            the assignment
	 * @return Grade the grade
	 */
	public Grade getGrade(String asgn) {
		double score = scores.get(asgn).score() / scores.get(asgn).maxScore();
		for (Grade grade : Grader.getCurve().getGrades()) {
			if (grade.min() <= score && grade.max() >= score) {
				return grade;
			}
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
		if (scores.get(asgn) != null && scores.get(asgn).score() != null) {
			return scores.get(asgn).score();
		}
		return null;
	}

	/**
	 * Adds an assignment for this student, where their score will be recorded.
	 * The assignment will have a default grade of zero
	 * 
	 * @param asgn
	 *            The name of the new assignment
	 */
	public void addAssignment(GradedItem asgn) {
		Debug.log("Adding assignment to student", "New assignment: " + asgn);
		if(!scores.values().contains(asgn)) {
			GradedItem item = asgn.copy();
			scores.put(item.name(), item);
			if(asgn.hasParent()) {
				setScore(asgn.getParent().name(), null);
				GradedItem parent = scores.get(asgn.getParent().name());
				if(parent != null && !parent.getChildren().contains(asgn)) {
					try {
						parent.addChild(asgn);
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Removes the student's reference to the assignment
	 */
	public void removeAssignment(GradedItem item) {
		if(item.hasParent()) {
			if(scores.get(item.getParent().name()) != null) {
				scores.get(item.getParent().name()).removeChild(item);
			}
			scores.remove(item.getParent().name());
		}
		scores.remove(item);
	}
	
	/**
	 * Gets the student's copy of a graded assignment with the 
	 * given name
	 */
	public GradedItem getAssignment(String name) {
		return scores.get(name);
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
		GradedItem item = scores.get(asgn);
		item.setScore(sc);
		scores.put(asgn, item);
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
		setScore(asgn, percent / 100 * Grader.getAssignment(asgn).maxScore());
	}

	/**
	 * Removes an assignment
	 * 
	 * @param asgn
	 *            The name of the assignment
	 */
	public void removeScore(String asgn) {
		scores.remove(asgn);
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
