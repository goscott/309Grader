package model.roster;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
 * @author Michael Lenz
 */
public class Roster implements Serializable{
	/**
     * generated serial ID
     */
    private static final long serialVersionUID = -8021729176053193523L;
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
	
	public double getScore(Student student, String asgn) {
		if (students.contains(student) && assignments.contains(asgn)) {
			Student stud = students.get(students.indexOf(student));
			return stud.getAssignmentScore(asgn);
		}
		return -1;
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
		try {
		    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(rost.courseName + ".rost"));
		    out.writeObject(rost);
		    out.close();
		}
		catch(IOException ex)
		{
		    System.err.println("failed to save Roster " + rost.courseName);
		}
	}
	public static Roster load(String url) {
	    Roster toReturn = null;
        try
        {
            FileInputStream in = new FileInputStream(url);
            ObjectInputStream obj = new ObjectInputStream(in);
            toReturn = (Roster) obj.readObject();
            
        }
        catch (FileNotFoundException e)
        {
            System.err.println("failed to find roster " + url.substring(0, url.length() - 4));
            
        }
        catch (IOException e)
        {
            System.err.println("failed to load roster " + url.substring(0, url.length() - 4));
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("failed to load roster " + url.substring(0, url.length() - 4));
        }
        return toReturn;
	        
	    
	}

}
