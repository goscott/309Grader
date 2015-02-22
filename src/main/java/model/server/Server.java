package model.server;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.announcements.Announcement;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Roster;
import model.roster.Student;

/**
 * A "Server" emulating PolyLearn
 * 
 * @author Gavin Scott
 * @author Shelli Crispen
 *
 */
public class Server {
	/** The students stored in the server **/
	private static ArrayList<Student> students = new ArrayList<Student>();
	/** An ObservableList of the students in the server **/
	private static ObservableList<Student> studentNames;
	/** A map of which classes a user is enrolled in or teaching **/
	private static HashMap<Student, ArrayList<String>> associatedClasses;
	private static HashMap<String, ArrayList<Announcement>> rosterAnnouncements;

	/**
	 * Gets the list of all students stored in the server
	 * 
	 * @return ArrayList<Student> the list of students
	 */
	public static ArrayList<Student> getStudents() {
		Debug.log("Accessing Server", "Students pulled from server");
		return students;
	}

	/**
	 * Searches the server for a student with the given ID. Returns
	 * the student if they exist in the server, or null if they do
	 * not.
	 */
	public static Student findStudent(String id) {
		for(Student student : students) {
			if(student.getId().equals(id)) {
				return student;
			}
		}
		return null;
	}
	
	/**
	 * Gets the list of all students stored in the server and turns the
	 * ArrayList to an ObservableList.
	 * 
	 * @return ObservableList<Student> the list of students
	 */
	/*
	 * @ requires (\forall student != null);
	 * 
	 * @
	 */
	public static ObservableList<Student> getObservableStudentList() {
		studentNames = FXCollections.observableArrayList();
		for (Student student : students) {
			studentNames.add(student);
		}
		return studentNames;
	}

	/**
	 * Gets the list of all students stored in the server and turns the
	 * ArrayList to an ObservableList. This only gets a list of the students not
	 * in the Roster.
	 * 
	 * @return ObservableList<Student> the list of students
	 */
	/*@ 
	 	requires (
	 		\forall student != null && roster.contains(student.id) == null
	 	);
	 @*/
	public static ObservableList<Student> getStudentListNotRoster() {
		studentNames = FXCollections.observableArrayList();
		for (Student student : students) {
			if (student != null
					&& Grader.getRoster().getStudentByID(student.getId()) == null) {
				studentNames.add(student);
			}
		}
		return studentNames;
	}

	/**
	 * Gets the list of all students stored in the roster and turns the
	 * ArrayList to an ObservableList.
	 * 
	 * @return ObservableList<Student> the list of students
	 */
	/*
	 * @ requires (\forall student != null && roster.contains(student.id) ==
	 * null);
	 * 
	 * @
	 */
	public static ObservableList<Student> getStudentListInRoster() {
		studentNames = FXCollections.observableArrayList();
		for (Student student : students) {
			if (student != null
					&& Grader.getRoster().getStudentByID(student.getId()) != null) {
				studentNames.add(student);
			}
		}
		return studentNames;
	}

	/**
	 * Gets the list of all students stored in the server and turns the
	 * ArrayList to an ObservableList.
	 * 
	 * @return ObservableList<Student> the list of student's names.
	 */
	/*
	 * @ requires (\forall student != null);
	 * 
	 * @
	 */
	public static ObservableList<String> getStudentListName() {
		ObservableList<String> studentNames = FXCollections
				.observableArrayList();
		for (Student student : students) {
			studentNames.add(student.getName());
		}
		return studentNames;
	}

	/**
	 * Gets the list of all students stored in the server and turns the
	 * ArrayList to an ObservableList. This only gets a list of the students not
	 * in the Roster.
	 * 
	 * @return ObservableList<Student> the list of student's names.
	 */
	/*
	 * @ requires (\forall student != null && roster.contains(student.id) ==
	 * null);
	 * 
	 * @
	 */
	public static ObservableList<String> getStudentListNameNotRoster() {
		ObservableList<String> studentNames = FXCollections
				.observableArrayList();
		for (Student student : students) {
			if (student != null
					&& Grader.getRoster().getStudentByID(student.getId()) == null) {
				studentNames.add(student.getName());
			}
		}
		return studentNames;
	}

	/**
	 * Gets the list of all students stored in the server and turns the
	 * ArrayList to an ObservableList. This only gets a list of the students not
	 * in the Roster.
	 * 
	 * @return ObservableList<Student> the list of student's names.
	 */
	/*
	 * @ requires (\forall student != null && roster.contains(student.id) ==
	 * null);
	 * 
	 * @
	 */
	public static ObservableList<String> getStudentListNameInRoster() {
		ObservableList<String> studentNames = FXCollections
				.observableArrayList();
		for (Student student : students) {
			if (student != null
					&& Grader.getRoster().getStudentByID(student.getId()) != null) {
				studentNames.add(student.getName());
			}
		}
		return studentNames;
	}

	/**
	 * Gets a list of the names of every roster that a user is associated with
	 */
	/*
	public static ArrayList<String> getAssociatedRosters(Student student) {
	return associatedClasses.get(student);
	}*/

	/**
	 * Gets a list of every user associated with the current roster
	 */
	/*
	public static ArrayList<Student> getAssociatedStudents(Roster roster) {
	 ArrayList<Student> students = new ArrayList<Student>();
	 String rName = roster.courseName();
	 for(Student student : associatedClasses.keySet()) {
	     if(associatedClasses.get(student).contains(rName))
	         students.add(student);
	 }
	 return students;
	}*/

	/**
	 * Gets a student from the server by their name
	 */
	public static Student getStudentByName(String name) {
		for(Student student : students) {
			if(student.getName().equals(name)) {
				return student;
			}
		}
		return null;
	}
	
	/**
	 * Associates a roster with this user
	 */
	public static void addRosterToUser(Student student, Roster roster) {

		if (associatedClasses.get(student) != null
				&& !associatedClasses.get(student)
						.contains(roster.courseName())) {
			Debug.log("Server updated", student.getName() + " associated with " + roster.courseName());
			associatedClasses.get(student).add(roster.courseName());
		} else {
			Debug.log("Error", "Null roster associated with student : removal failed");
		}
	}

	/**
	 * Gets a list of all students associated with a roster (in the server)
	 */
	public static ArrayList<Student> getStudentsAssociatedWithRoster(Roster roster) {
		ArrayList<Student> list = new ArrayList<Student>();
		for(Student student : associatedClasses.keySet()) {
			if(associatedClasses.get(student).contains(roster.courseName())) {
				list.add(student);
			}
		}
		return list;
	}
	
	/**
	 * Disassociates a roster with this user
	 */
	public static void removeRosterFromUser(Student student, Roster roster) {
		if (associatedClasses.get(student) != null) {
			Debug.log("Server updated", student.getName() + " no longer associated with " + roster.courseName());
			associatedClasses.get(student).remove(roster.courseName());
		} else {
			Debug.log("Error", "Null roster associated with student : removal failed");
		}
	}

	/**
	 * Adds an announcement to the server for the current roster
	 */
	public static void addAnnouncement(Announcement announcement) {
		Debug.log("Server", "Announcement stored in server");
		Roster roster = Grader.getRoster();
		ArrayList<Announcement> anns;
		if(rosterAnnouncements.get(roster.courseName()) != null) {
			anns = rosterAnnouncements.get(roster.courseName());
		} else {
			anns = new ArrayList<Announcement>();
		}
		anns.add(announcement);
		rosterAnnouncements.put(roster.courseName(), anns);
	}
	
	/**
	 * Gets all announcements associated with a roster
	 */
	public static ArrayList<Announcement> getAssociatedAnnouncements(Roster roster) {
		return rosterAnnouncements.get(roster.courseName());
	}
	
	/**
	 * Populates the server's list of students
	 */
	private static void initializeStudents() {
		students.add(new Student("Jim", "00000"));
		students.add(new Student("Tim", "11111"));
		students.add(new Student("Gavin Scott", "12345"));
		students.add(new Student("Shelli Crispen", "21549"));
		students.add(new Student("Frank Poole", "34718"));
		students.add(new Student("Mason Stevenson", "87123"));
		students.add(new Student("Michael Lenz", "98012"));
		students.add(new Student("Jacob Hardi", "01968"));
		
		associatedClasses = new HashMap<Student, ArrayList<String>>();
		for (Student student : students) {
			associatedClasses.put(student, new ArrayList<String>());
		}
		
		Debug.log("Initializing Server", "Students Loaded");
	}
	
	/**
	 * Populates the servers list of announcements
	 */
	private static void initializeAnnouncements() {
		rosterAnnouncements = new HashMap<String, ArrayList<Announcement>>();
		Debug.log("Initializing Server", "Announcements Loaded");
	}
	
	/**
	 * Populates the server with some default students
	 */
	public static void init() {
		Debug.log("Server initialization", "starting init...");
		initializeStudents();
		initializeAnnouncements();
	}
	
	/**
	 * Saves the announcements stored in the server
	 */
	private static void backupAnnouncements() {
		Debug.log("Server Backup", "Announcements backed up");
	}
	
	/**
	 * Saves the associated classes stored in the server
	 */
	private static void backupAssociatedClasses() {
		Debug.log("Server Backup", "Class associations backed up");
	}
	
	/**
	 * Commits the new server data to files so they will be preserved
	 * between runs of the program
	 */
	public static void backup() {
		Debug.log("Server Backup", "Starting backup...");
		backupAnnouncements();
		backupAssociatedClasses();
	}
}
