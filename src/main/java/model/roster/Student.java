package model.roster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import model.driver.Grader;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;

/**
 * A student in a class
 * @author Gavin Scott
 *
 */
public class Student implements Comparable<Student>, Serializable {
	/**
     * generated serial ID
     */
    private static final long serialVersionUID = 6298208303690715171L;
    private final SimpleStringProperty name;
	private final SimpleStringProperty id;
	private HashMap<String, GradedItem> scores;
	private double totalScore;

	/**
	 * Creates a student with the given information
	 * @param name The student's name
	 * @param id The student's ID
	 */
	public Student(String name, String id) {
		this.name = new SimpleStringProperty(name);
		this.id = new SimpleStringProperty(id);
		scores = new HashMap<String, GradedItem>();
		totalScore = 0;
	}

	/**
	 * Gets the student's name
	 * @return String the name of the student
	 */
	public String getName() {
		return name.get();
	}

	/**
	 * Gets the student's ID
	 * @return String the student's ID
	 */
	public String getId() {
		return id.get();
	}

	/**
	 * Gets the student's overall grade
	 * @return double the total grade
	 */
	public double getTotalScore() {
		return totalScore;
	}

	/**
	 * Gets the student's score on an individual
	 * assignment
	 * @param asgn The name of the assignment
	 * @return Double the student's score on the
	 * assignment
	 */
	public Double getAssignmentScore(String asgn) {
		return scores.get(asgn)!=null ? scores.get(asgn).score() : null;
	}
	
	/**
	 * Gets the student's score on an individual
	 * assignment
	 * @param asgn The name of the assignment
	 * @return SimpleDoubleProperty the student's 
	 * score on the assignment
	 */
	public SimpleDoubleProperty getAssignmentScoreAsProperty(String asgn) {
		if(getAssignmentScore(asgn) != null)
			return new SimpleDoubleProperty(getAssignmentScore(asgn));
		else
			return new SimpleDoubleProperty(-1);
	}

	/**
	 * Adds an assignment for this student, where their score
	 * will be recorded. The assignment will have a default 
	 * grade of zero
	 * @param asgn The name of the new assignment
	 */
	public void addAssignment(GradedItem asgn) {
		GradedItem item = asgn.copy();
		item.setScore(0);
		scores.put(item.name(), item);
	}

	/**
	 * Sets the score for an assignment
	 * @param asgn The name of the assignment
	 * @param sc the new score
	 */
	public void setScore(String asgn, double sc) {
		GradedItem item = scores.get(asgn);
		item.setScore(sc);
		scores.put(asgn, item);
		calcTotalScore();
	}
	
	/**
	 * Sets the assignment's score to a percentage of the assignment's maximum
	 * score
	 * @param asgn the name of the assignment
	 * @param percent
	 *            the percent (90.0, etc)
	 */
	public void setPercentScore(String asgn, double percent) {
		setScore(asgn, percent/100*Grader.getAssignment(asgn).maxScore());
	}

	/**
	 * Removes an assignment
	 * @param asgn The name of the assignment
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
	 * @param other The other object
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
	 * @param students the list
	 * @return String a representation of the list of students
	 */
	public static String Save(ArrayList<Student> students) {
		String toReturn = "";
		char secret = 1;
		for (Student stu : students) {
			toReturn += "S" + secret;
			toReturn += stu.name.toString() + secret + stu.id + secret;
			//toReturn += ScoreNode.Save(stu.scores);
			toReturn += "\n";
		}

		return toReturn;
	}
	
	/**
	 * Sets the total score of a student based on the
	 * sum of the scores for every assignment
	 */
	private void calcTotalScore() {
		totalScore = 0;
		for(GradedItem item : scores.values()) {
			totalScore += item.score();
		}
	}
}
