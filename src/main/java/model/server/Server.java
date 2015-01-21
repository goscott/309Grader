package model.server;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import model.driver.Debug;
import model.roster.Student;

/**
 * A "Server" emulating PolyLearn
 * @author Gavin Scott
 * @author Shelli Crispen
 *
 */
public class Server {
	private static ArrayList<Student> students = new ArrayList<Student>();
	private static ObservableList<String> studentNames;
	
	/**
	 * Gets the list of all students stored in the
	 * server
	 * @return ArrayList<Student> the list of students
	 */
	public static ArrayList<Student> getStudents() {
		Debug.log("Accessing Server", "Students pulled from server");
		return students;
	}
	
	public static ObservableList<String> getObserableStudentList() {
		for(Student student: students){
			studentNames.add(student.getName());
		}
		return studentNames;
	}
	
	/**
	 * Populates the server with some default students
	 */
	public static void init() {
		students.add(new Student("Bill", "00000"));
		students.add(new Student("Bob", "11111"));
		students.add(new Student("Gavin Scott", "12345"));
		students.add(new Student("Shelli Crispen", "21549"));
		students.add(new Student("Frank Poole", "34718"));
		students.add(new Student("Mason Stevenson", "87123"));
		students.add(new Student("Michael Lenz", "98012"));
		students.add(new Student("Jacob Hardi", "01968"));
		Debug.log("Initializing Server", "Server students populated");
	}
}
