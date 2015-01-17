package model.roster;

import java.util.ArrayList;
import java.util.HashMap;

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
public class Student implements Comparable<Student> {
	private final SimpleStringProperty name;
	private final SimpleStringProperty id;
	private HashMap<String, ScoreNode> scores;
	private HashMap<String, Double> scoreValues;
	private double totalScore;

	public Student(String name, String id) {
		this.name = new SimpleStringProperty(name);
		this.id = new SimpleStringProperty(id);
		scores = new HashMap<String, ScoreNode>();
		scoreValues = new HashMap<String, Double>();
		totalScore = 0;
	}

	public String getName() {
		return name.get();
	}

	public String getId() {
		return id.get();
	}

	public double getTotalScore() {
		return totalScore;
	}

	public Double getAssignmentScore(String asgn) {
		System.out.println("!!! getting score for " + asgn + ": " + scoreValues.get(asgn));
		return scoreValues.get(asgn);
	}
	
	public SimpleDoubleProperty getAssignmentScoreAsProperty(String asgn) {
		System.out.println("checking score for " + asgn);
		if(getAssignmentScore(asgn) != null)
			return new SimpleDoubleProperty(getAssignmentScore(asgn));
		else
			return new SimpleDoubleProperty(-1);
	}

	public void addAssignment(String asgn) {
		scores.put(asgn, new ScoreNode("temp", 0));
		System.out.println("adding assignment: " + asgn);
		scoreValues.put(asgn, 0.0);
	}

	public void setScore(String asgn, double sc) {
		//scores.put(asgn, sc);
		scoreValues.put(asgn, sc);
	}

	public void removeScore(String asgn) {
		scores.remove(asgn);
	}

	public int compareTo(Student other) {
		return name.toString().compareTo(other.name.toString());
	}

	public boolean equals(Object other) {
		if ((other != null) && (other instanceof Student)) {
			Student oth = (Student) other;
			return oth.getName().equals(name) && oth.getId().equals(id);
		}
		return false;
	}

	public static String Save(ArrayList<Student> students) {
		String toReturn = "";
		char secret = 1;
		for (Student stu : students) {
			toReturn += "S" + secret;
			toReturn += stu.name.toString() + secret + stu.id + secret;
			toReturn += ScoreNode.Save(stu.scores);
			toReturn += "\n";
		}

		return toReturn;
	}
}
