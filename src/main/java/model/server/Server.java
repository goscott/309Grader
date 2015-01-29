package model.server;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Student;

/**
 * A "Server" emulating PolyLearn
 * @author Gavin Scott
 * @author Shelli Crispen
 *
 */
public class Server {
	/** The students stored in the server **/
	private static ArrayList<Student> students = new ArrayList<Student>();
	/** An ObservableList of the students in the server **/
	private static ObservableList<Student> studentNames;
	
	/**
	 * Gets the list of all students stored in the
	 * server
	 * @return ArrayList<Student> the list of students
	 */
	public static ArrayList<Student> getStudents() {
		Debug.log("Accessing Server", "Students pulled from server");
		return students;
	}
	
	/**
	 * Gets the list of all students stored in the
	 * server and turns the ArrayList to an ObservableList.
	 * @return ObservableList<Student> the list of students
	 */
	//@ requires students != null;
    //@ assignable \forall studentNames;
	public static ObservableList<Student> getObservableStudentList() { 
	    studentNames = FXCollections.observableArrayList ();
		for(Student student: students){
			studentNames.add(student);
		}
		return studentNames;
	}
	
	/**
	 * Gets the list of all students stored in the
	 * server and turns the ArrayList to an ObservableList.
	 * This only gets a list of the students not in the Roster.
	 * @return ObservableList<Student> the list of students
	 */
	//@ requires student != null && student.id == null;
    //@ assignable \forall studentNames;
	public static ObservableList<Student> getStudentListNotRoster() { 
	    studentNames = FXCollections.observableArrayList ();
		for(Student student: students){
			if(student != null && Grader.getRoster().getStudentByID(student.getId()) == null){
				studentNames.add(student);
			}
		}
		return studentNames;
	}
	
	/**
	 * Gets the list of all students stored in the
	 * server and turns the ArrayList to an ObservableList.
	 * @return ObservableList<Student> the list of student's names.
	 */
	//@ requires \forall students != null;
    //@ assignable studentNames;
	public static ObservableList<String> getStudentListName() { 
		ObservableList<String> studentNames = FXCollections.observableArrayList ();
		for(Student student: students){
			studentNames.add(student.getName());
		}
		return studentNames;
	}
	

	/**
	 * Gets the list of all students stored in the
	 * server and turns the ArrayList to an ObservableList.
	 * This only gets a list of the students not in the Roster.
	 * @return ObservableList<Student> the list of student's names.
	 */
	//@ requires \forall student != null && student.id == null;
    //@ assignable studentNames;
	public static ObservableList<String> getStudentListNameNotRoster() { 
		ObservableList<String> studentNames = FXCollections.observableArrayList ();
		for(Student student: students){
			if(student != null && Grader.getRoster().getStudentByID(student.getId()) == null){
				studentNames.add(student.getName());
			}
		}
		return studentNames;
	}
	
	/**
	 * Populates the server with some default students
	 */
	public static void init() {
		students.add(new Student("Jim", "00000"));
		students.add(new Student("Tim", "11111"));
		students.add(new Student("Gavin Scott", "12345"));
		students.add(new Student("Shelli Crispen", "21549"));
		students.add(new Student("Frank Poole", "34718"));
		students.add(new Student("Mason Stevenson", "87123"));
		students.add(new Student("Michael Lenz", "98012"));
		students.add(new Student("Jacob Hardi", "01968"));
		Debug.log("Initializing Server", "Server students populated");
	}
}
