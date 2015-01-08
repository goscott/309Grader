package roster;

import java.util.HashMap;

public class Student implements Comparable<Student> {
	private final String name;
	private final String id;
	private HashMap<String, GradedItem> scores;

	public Student(String name, String id) {
		this.name = name;
		this.id = id;
		scores = new HashMap<String, GradedItem>();
	}

	public double getTotalScore() {
		return 0;
	}
	
	public GradedItem getAssignmentScore(String asgn) {
		return scores.get(asgn);
	}
	
	public void addScore(String asgn, GradedItem item) {
		scores.put(asgn, item);
	}
	
	public void removeScore(String asgn) {
		scores.remove(asgn);
	}
	
	public int compareTo(Student other) {
		return name.compareTo(other.name);
	}
}
