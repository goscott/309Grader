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

import model.curve.Curve;
import model.driver.Debug;

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
	private Curve curve;

	/**
	 * Creates a roster with the given information
	 * @param name The name of the course
	 * @param time The time (i.e. quarter, year, etc.)
	 * of the course.
	 * @param instructor The name of the instructor
	 */
	public Roster(String name, String time, String instructor) {
		courseName = name;
		this.time = time;
		students = new ArrayList<Student>();
		assignments = new ArrayList<GradedItem>();
		ids = new HashMap<String, Student>();
		this.instructor = instructor;
		curve = new Curve();
	}

	/**
	 * Adds a student to the course roster, giving
	 * the new student a default grade for every
	 * existing assignment in the roster
	 * @param student the new student
	 */
	public void addStudent(Student student) {
		students.add(student);
		ids.put(student.getId(), student);
		for(GradedItem item : assignments) {
			student.addAssignment(item);
		}
	}

	/**
	 * Gets the course instructor
	 * @return String the instructor
	 */
	public String getInstructor() {
		return instructor;
	}

	/**
	 * Gets the course time
	 * @return String the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Gets the course name
	 * @return String the course name
	 */
	public String courseName() {
		return courseName;
	}

	/**
	 * Adds an assignment to the course
	 * @param asgn The GradedItem being added to
	 * the roster
	 */
	public void addAssignment(GradedItem asgn) {
		assignments.add(asgn);
		for (Student stud : students) {
			stud.addAssignment(asgn);
		}
	}

	/**
	 * Gets a reference to the assignment with the
	 * given name in the roster, if it exists
	 * @param name the assignment name that will be
	 * searched for.
	 * @return GradedItem the GradedItem in the roster
	 * with that name
	 */
	public GradedItem getAssignment(String name) {
		for (GradedItem item : assignments)
			if (item.name().equals(name))
				return item;
		return null;
	}

	/**
	 * Scores a student for a particular assignment.
	 * @param student The student being scored
	 * @param asgn The assignment being scored
	 * @param score The score that will be recorded in
	 * the roster for this particular student and 
	 * assignment.
	 */
	public void addScore(Student student, GradedItem asgn, double score) {
		if (students.contains(student) && assignments.contains(asgn)) {
			Student stud = students.get(students.indexOf(student));
			stud.setScore(asgn.name(), score);
		}
	}
	
	/**
	 * Gets the score of a student on an assignment with the
	 * given name
	 * @param student The student in question
	 * @param asgn The name of the assignment
	 * @return double the student's score on the assignment
	 */
	public double getScore(Student student, String asgn) {
		if (students.contains(student) && assignments.contains(asgn)) {
			Student stud = students.get(students.indexOf(student));
			return stud.getAssignmentScore(asgn);
		}
		return -1;
	}

	/**
	 * Searches for a student by their ID
	 * @param id the ID of the student
	 * @return the student, or null if no student
	 * with that id exists in the roster
	 */
	public Student getStudentByID(String id) {
		return ids.get(id);
	}

	/**
	 * Checks if a student with the given ID
	 * exist in the roster
	 * @param id The ID of the student
	 * @return boolean true if there is a 
	 * student with that ID in the roster
	 */
	public boolean containsStudent(String id) {
		return students.contains(id);
	}

	/**
	 * Gets the number of students in the roster
	 * @return int the number of students
	 */
	public int numStudents() {
		return students.size();
	}

	/**
	 * Gets all the students in the roster, sorted
	 * by name.
	 * @return A sorted list of all of the students
	 * in the roster
	 */
	public ArrayList<Student> getStudentsByName() {
		Collections.sort(students);
		return students;
	}

	/**
	 * Gets all the students in the roster, sorted
	 * by score.
	 * @return A sorted list of all of the students
	 * in the roster
	 */
	public ArrayList<Student> getStudentsByScore() {
		Collections.sort(students, new ScoreComparator());
		return students;
	}

	/**
	 * Gets all the students in the roster, sorted
	 * by ID.
	 * @return A sorted list of all of the students
	 * in the roster
	 */
	public ArrayList<Student> getStudentsById() {
		Collections.sort(students, new IDComparator());
		return students;
	}
	
	/**
	 * Compares two students by their scores.
	 * @author Gavin Scott
	 *
	 */
	private class ScoreComparator implements Comparator<Student> {
		/**
		 * Compares two students by their scores
		 * @param s1 The first student
		 * @param s2 The second student
		 * @return int the comparison value
		 */
		public int compare(Student s1, Student s2) {
			return (int) (s1.getTotalScore() - s2.getTotalScore());
		}

	}

	/**
	 * Compares two students by their IDs.
	 * @author Gavin Scott
	 *
	 */
	private class IDComparator implements Comparator<Student> {
		/**
		 * Compares two students by their IDs
		 * @param s1 The first student
		 * @param s2 The second student
		 * @return int the comparison value
		 */
		public int compare(Student s1, Student s2) {
			return s1.getId().compareTo(s2.getId());
		}

	}
	
	/**
	 * Checks this roster with an object for equality
	 * @return boolean true if they are logically equal
	 */
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

	/**
	 * Gets a list of all the assignments associated
	 * with this roster
	 * @return The list of GradedItems
	 */
	public ArrayList<GradedItem> getAssignments() {
		return assignments;
	}
	
	/**
	 * Gets all assignments associated with this roster,
	 * sorted by depth (deepest first)
	 * @return The sorted list of GradedItems
	 */
	public ArrayList<GradedItem> getAssignmentsByDepth() {
		ArrayList<GradedItem> sortedList = new ArrayList<GradedItem>();
		for(GradedItem item : assignments) {
			sortedList.add(item.copy());
		}
		sortedList.sort(new DepthComparator());
		return sortedList;
	}

	/**
	 * Returns a string representation of the roster
	 * @return String the representation
	 */
	public String toString() {
		return courseName + " " + time + " " + instructor;
	}

	/**
	 * Saves the roster to the computer
	 */
	public void Save() {
		save(this);
	}

	/**
	 * Saves a roster to the computer
	 * @param rost The roster to be saved
	 */
	public static void save(Roster rost) {
		try {
		    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(rost.courseName + ".rost"));
		    out.writeObject(rost);
		    out.close();
		}
		catch(IOException ex)
		{
			Debug.log("SAVE ERROR", "failed to save Roster " + rost.courseName);
		}
	}
	
	/**
	 * Loads a roster from the computer
	 * @param url the path to the roster
	 * @return Roster the loaded roster
	 */
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
        	Debug.log("IO ERROR", "Could not locate file at " + url.substring(0, url.length() - 4));
            
        }
        catch (IOException e)
        {
            Debug.log("IO ERROR", "Could not locate file at " + url.substring(0, url.length() - 4) + 
            		"(IOException)");
        }
        catch (ClassNotFoundException e)
        {
            Debug.log("IO ERROR", "Could not locate file at " + url.substring(0, url.length() - 4) + 
            		"(Class Not Found)");
        }
        return toReturn; 
	}
	
	private class DepthComparator implements Comparator<GradedItem> {
		/**
		 * Compares two GradedItems by depth
		 * @param item1 The first assignment
		 * @param item2 The second assignment
		 * @return int the comparison value
		 */
		public int compare(GradedItem item1, GradedItem item2) {
			return item1.getDepth() - item2.getDepth();
		}

	}

	/**
	 * Gets the maximum points possible for all
	 * assignments
	 * @return double the max points
	 */
	public double getMaxPoints() {
		double max = 0;
		for(GradedItem item : assignments) {
			if(!item.isExtraCredit()) {
				max += item.maxScore();
			}
		}
		return max;
	}
	
	/**
	 * Sets the curve
	 * @param curve the new curve
	 */
	public void setCurve(Curve curve) {
		this.curve = curve;
	}
	
	/**
	 * Gets the curve
	 * @return the curve
	 */
	public Curve getCurve() {
		return curve;
	}
}
