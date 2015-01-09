package roster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Roster {
	private ArrayList<Student> students;
	private ArrayList<GradedItem> assignments;
	
	public Roster() {
		students = new ArrayList<Student>();
		assignments = new ArrayList<GradedItem>();
	}
	
	public void addStudent(Student s) {
		students.add(s);
	}
	
	public void addAssignment(GradedItem asgn) {
		assignments.add(asgn);
		for(Student stud : students) {
			stud.addAssignment(asgn.name(), asgn.copy());
		}
	}
	
	public void addScore(Student student, GradedItem asgn, ScoreNode score) {
		if(students.contains(student) && assignments.contains(asgn)) {
			System.out.println("score added");
			Student stud = students.get(students.indexOf(student));
			stud.changeScore(asgn.name(), score);
		}
	}
	
	private ArrayList<Student> getStudentsByName() {
		Collections.sort(students);
		return students;
	}
	
	private ArrayList<Student> getStudentsByScore() {
		Collections.sort(students, new ScoreComparator());
		return students;
	}
	
	private ArrayList<Student> getStudentsByAssignmentScore(String asgn) {
		Collections.sort(students, new AssignmentComparator(asgn));
		return students;
	}
	
	private class ScoreComparator implements Comparator<Student> {
		public int compare(Student s1, Student s2) {
			return (int) (s1.getTotalScore() - s2.getTotalScore());
		}
		
	}
	
	private class AssignmentComparator implements Comparator<Student> {
		private String asgn;
		
		public AssignmentComparator(String asgn)
		{
			this.asgn = asgn;
		}
		
		public int compare(Student s1, Student s2) {
			return (int) (s1.getAssignmentScore(asgn).score().value()
					- s2.getAssignmentScore(asgn).score().value());
		}
		
	}
	
	// TODO Delete (only for debugging)
	public void print() {
		getStudentsByName();
		System.out.println("============================================");
		System.out.println("ROSTER");
		System.out.println("============================================");
		System.out.print("\t\t\t\t\t\t\t");
		for(GradedItem a : assignments) {
			System.out.print(a.name() + '\t');
		}
		System.out.println();
		System.out.println("============================================");
		for(Student s : students) {
			System.out.print(s.name() + "\t\t" + s.id() + "\t\t");
			System.out.print("Total Score: " + s.getTotalScore() + "\t");
			for(GradedItem a : assignments) {
				if(s.getAssignmentScore(a.name()).score() != null) {
					System.out.print(s.getAssignmentScore(a.name()).score().value());
				}
				System.out.print('\t');
			}
			System.out.println();
		}
		System.out.println("============================================");
	}
}
