package model.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Roster;
import model.roster.Student;

/**
 * A "Server" emulating PolyLearn
 * 
 * @author Gavin Scott
 * @author Shelli Crispen
 */
public class Server {
	/** The students stored in the server **/
	private static ArrayList<Student> students = new ArrayList<Student>();
	/** An ObservableList of the students in the server **/
	private static ObservableList<Student> studentNames;
	/** A map of which classes a user is enrolled in or teaching **/
	private static HashMap<String, ArrayList<String>> associatedClasses;

	/**
	 * Gets the list of all students stored in the server
	 * 
	 * @return ArrayList<Student> the list of students
	 */
	/*@
	    ensures(
	        \result.equals(students)
	    );
	@*/
	public static ArrayList<Student> getStudents() {
		Debug.log("Accessing Server", "Students pulled from server");
		return students;
	}

	/**
	 * Searches the server for a student with the given ID. Returns the student
	 * if they exist in the server, or null if they do not.
	 */
	/*@ 
	    ensures(
	        (\forall Student student; students.contains(student); student.getId().equals(id))
	    );
	@*/
	public static Student findUser(String id) {
		for (Student student : students) {
			if (student.getId().equals(id)) {
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
	/*@ 
	    ensures(
	        (\forall Student student; students.contains(student); studentNames.contains(student.getName()))
	    );
	@*/
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
	    ensures(
	        (\forall Student student; students.contains(student); studentNames.contains(student))
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
	/*@ 
	    ensures(
	        (\forall Student student; students.contains(student); studentNames.contains(student))
	    );
	@*/
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
	/*@ 
	    ensures(
	        (\forall Student student; students.contains(student); studentNames.contains(student.getName()))
	    );
	@*/
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
	/*@ 
	    ensures(
	        (\forall Student student; students.contains(student); studentNames.contains(student.getName()))
	    );
	@*/
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
	/*@ 
	     ensures(
	         (\forall Student student; students.contains(student); studentNames.contains(student.getName()))
	     );
	@*/
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
	 * Gets a student from the server by their name
	 */
	/*@
	     ensures(
	         (\forall Student student; students.contains(student); student.getName().equals(name))  
	     );
	@*/
	public static Student getStudentByName(String name) {
		for (Student student : students) {
			if (student.getName().equals(name)) {
				return student;
			}
		}
		return null;
	}

	/**
	 * Associates a roster with this user
	 */
	/*@
	     ensures(
	         associatedClasses != null
	             &&
	         associatedClasses.get(student) != null
	             &&
	         !associatedClasses.get(student).contains(roster.courseName()))
	     );
	@*/
	public static void addRosterToUser(String id, Roster roster) {
		if (associatedClasses == null) {
			associatedClasses = new HashMap<String, ArrayList<String>>();
		}
		if (associatedClasses != null && associatedClasses.get(id) != null
				&& !associatedClasses.get(id).contains(roster.courseName())) {
			Debug.log("Server updated",
					id + " associated with " + roster.courseName());
			associatedClasses.get(id).add(roster.courseName());
		} else {
			Debug.log("Error",
					"Null roster associated with student : removal failed");
		}
	}

	/**
	 * Gets a list of all students associated with a roster (in the server)
	 */
	/*@
	     ensures(
	         (\forall Student student; associatedClasses.keySet().contains(student); list.contains(student)  
	     );
	@*/
	public static ArrayList<String> getStudentsAssociatedWithRoster(
			Roster roster) {
		ArrayList<String> list = new ArrayList<String>();
		if (associatedClasses != null) {
			for (String id : associatedClasses.keySet()) {
				if (associatedClasses.get(id).contains(roster.courseName())) {
					list.add(id);
				}
			}
		}
		return list;
	}

	/**
	 * Disassociates a roster with this user
	 */
	/*@
	     requires(
	         associatedClasses.get(student) != null
	     );
	     ensures
	     (   *
	         *Gavin's Debug functions to help make sure the server is running correctly.
	         *Also removes the roster from the users profiles  
	     );
	@*/
	public static void removeRosterFromUser(Student student, Roster roster) {
		if (associatedClasses.get(student) != null) {
			Debug.log("Server updated", student.getName()
					+ " no longer associated with " + roster.courseName());
			associatedClasses.get(student).remove(roster.courseName());
		} else {
			Debug.log("Error",
					"Null roster associated with student : removal failed");
		}
	}

	/**
	 * Populates the server's list of students
	 */
	/*@
	     ensures(
	         (\forall Student student; students.contains(student); student != null)
	     );
	@*/
	private static void initializeStudents() {
		PeopleSoftServer.initialize();
		for (Student student : PeopleSoftServer.getStudents()) {
			students.add(student);
		}

		associatedClasses = new HashMap<String, ArrayList<String>>();
		for (Student student : students) {
			associatedClasses.put(student.getId(), new ArrayList<String>());
		}

		Debug.log("Initializing Server", "Students Loaded");
	}

	/**
	 * Populates the server
	 */
	/*@
	    ensures
	    (   *
	        *Gavin's Debug functions to help make sure the server is running correctly. 
	    );
	@*/
	@SuppressWarnings("unchecked")
	public static void init() {

		initializeStudents();

		Debug.log("Server initialization", "starting init...");
		try {
			FileInputStream in = new FileInputStream("server.sav");
			ObjectInputStream obj = new ObjectInputStream(in);
			students = (ArrayList<Student>) obj.readObject();
			associatedClasses = (HashMap<String, ArrayList<String>>) obj
					.readObject();
			obj.close();
			Debug.log("Server", "Sucessfuly loaded");

		} catch (FileNotFoundException e) {
			Debug.log("No Server loaded", "initialize default");
			initializeStudents();

		} catch (IOException e) {
			Debug.log("IO ERROR", "Could not locate file at server.sav"
					+ "(IOException)");
		} catch (ClassNotFoundException e) {
			Debug.log("IO ERROR", "Could not locate file at server.sav"
					+ "(Class Not Found)");
		}
	}

	/**
	 * Commits the new server data to files so they will be preserved between
	 * runs of the program
	 */
	/*@
	     ensures
	     (   *
	         *Gavin's Debug functions to help make sure the server is running correctly. 
	     );
	@*/
	public static void backup() {
		Debug.log("Server Backup", "Starting backup...");
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("server.sav"));
			out.writeObject(students);
			out.writeObject(associatedClasses);
			out.close();
		} catch (IOException ex) {
			Debug.log("SAVE ERROR", "failed to backup server");
			ex.printStackTrace();
		}
	}
}
