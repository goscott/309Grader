package roster;

import java.util.HashMap;

public class Student implements Comparable<Student> {
	private final String name;
	private final String id;
	private HashMap<String, ScoreNode> scores;

	public Student(String name, String id) {
		this.name = name;
		this.id = id;
		scores = new HashMap<String, ScoreNode>();
	}
	
	public String name() {
		return name;
	}
	
	public String id() {
		return id;
	}

	public double getTotalScore() {
		return 0;
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
		return name.compareTo(other.name);
	}
	
	public boolean equals(Object other) {
		boolean ret = false;
		if((other != null) && (other instanceof Student)) {
			Student oth = (Student)other;
			return oth.name().equals(name) && oth.id().equals(id);
		}
		return ret;
	}
}
