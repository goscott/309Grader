package roster;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student implements Comparable<Student> {
	private final SimpleStringProperty name;
	private final SimpleStringProperty id;
	private HashMap<String, ScoreNode> scores;
	private double totalScore;

	public Student(String name, String id) {
		this.name = new SimpleStringProperty(name);
		this.id = new SimpleStringProperty(id);
		scores = new HashMap<String, ScoreNode>();
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

	public ScoreNode getAssignmentScore(String asgn) {
		return scores.get(asgn);
	}

	public void addAssignment(String asgn) {
		scores.put(asgn, null);
	}

	public void changeScore(String asgn, ScoreNode sc) {
		scores.put(asgn, sc);
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
