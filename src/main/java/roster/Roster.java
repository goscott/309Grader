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
	
	private ArrayList<Student> getStudentsByName() {
		Collections.sort(students);
		return students;
	}
	
	private ArrayList<Student> getStudentsByScore() {
		Collections.sort(students, new ScoreComparator());
		return students;
	}
	
	private class ScoreComparator implements Comparator<Student> {
		public int compare(Student s1, Student s2) {
			return (int) (s1.getTotalScore() - s2.getTotalScore());
		}
		
	}
}
