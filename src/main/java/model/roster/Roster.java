package model.roster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The class Roster that stores students and assignments
 * 
 * @author Gavin Scott
 */
public class Roster {
	private String courseName;
	private String instructor;
	private String time;
	private ArrayList<Student> students;
	private ArrayList<GradedItem> assignments;
	private HashMap<String, Student> ids;

	public Roster(String name, String time) {
		courseName = name;
		this.time = time;
		students = new ArrayList<Student>();
		assignments = new ArrayList<GradedItem>();
		ids = new HashMap<String, Student>();
	}

	public void addStudent(Student s) {
		students.add(s);
		ids.put(s.getId(), s);
		for(GradedItem item : assignments) {
			s.addAssignment(item.name());
		}
	}

	public String getInstructor() {
		return instructor;
	}

	public String getTime() {
		return time;
	}

	public String courseName() {
		return courseName;
	}

	public void addAssignment(GradedItem asgn) {
		assignments.add(asgn);
		for (Student stud : students) {
			stud.addAssignment(asgn.name());
		}
	}

	public GradedItem getAssignment(String name) {
		for (GradedItem item : assignments)
			if (item.name().equals(name))
				return item;
		return null;
	}

	public void addScore(Student student, GradedItem asgn, ScoreNode score) {
		if (students.contains(student) && assignments.contains(asgn)) {
			Student stud = students.get(students.indexOf(student));
			stud.setScore(asgn.name(), score.value());
		}
	}

	public Student getStudentByID(String id) {
		return ids.get(id);
	}

	public boolean containsStudent(String id) {
		return students.contains(id);
	}

	public int numStudents() {
		return students.size();
	}

	public ArrayList<Student> getStudentsByName() {
		Collections.sort(students);
		return students;
	}

	public ArrayList<Student> getStudentsByScore() {
		Collections.sort(students, new ScoreComparator());
		return students;
	}

	public ArrayList<Student> getStudentsById() {
		Collections.sort(students, new IDComparator());
		return students;
	}

	/*public ArrayList<Student> getStudentsByAssignmentScore(String asgn) {
		Collections.sort(students, new AssignmentComparator(asgn));
		return students;
	}*/
	
	private class ScoreComparator implements Comparator<Student> {
		public int compare(Student s1, Student s2) {
			return (int) (s1.getTotalScore() - s2.getTotalScore());
		}

	}

	private class IDComparator implements Comparator<Student> {
		public int compare(Student s1, Student s2) {
			return s1.getId().compareTo(s2.getId());
		}

	}
/*
	private class AssignmentComparator implements Comparator<Student> {
		private String asgn;

		public AssignmentComparator(String asgn) {
			this.asgn = asgn;
		}

		public int compare(Student s1, Student s2) {
			return (int) (s1.getAssignmentScore(asgn).value() - s2
					.getAssignmentScore(asgn).value());
		}

	}*/

	/*public boolean equals(Object other) {
		return true;
		/*if ((other == null) || !(other instanceof GradedItem)) {
			return false;
		}
		Roster rost = (Roster) other;
		return rost.courseName().equals(courseName);
	}*/
	
	public boolean equals(Object other) {
		if ((other == null) || !(other instanceof GradedItem)) {
			return false;
		}
		Roster rost = (Roster) other;
		if (!rost.courseName().equals(courseName)
				|| !rost.getTime().equals(time)
				|| !rost.getInstructor().equals(instructor)
				|| rost.getAssignments().size() != assignments.size()
				|| rost.numStudents() != students.size()) {
			return false;
		}
		ArrayList<GradedItem> rostAsgn = rost.getAssignments();
		for (GradedItem item : assignments) {
			if (!rostAsgn.contains(item))
				return false;
		}
		for (String id : ids.keySet()) {
			if (!rost.containsStudent(id))
				return false;
		}
		return true;
	}

	public ArrayList<GradedItem> getAssignments() {
		return assignments;
	}

	public String toString() {
		return courseName + " " + time;
	}

	public void Save() {
		save(this);
	}

	public static void save(Roster rost) {
		char secret = 1;
		String save = rost.courseName + secret + rost.instructor + secret
				+ rost.time + "\n";
		save += GradedItem.Save(rost.assignments);
		save += Student.Save(rost.students);
	}

}
