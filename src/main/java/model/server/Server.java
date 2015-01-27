package model.server;

import java.util.ArrayList;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.curve.Curve;
import model.curve.Grade;
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
	private static Curve curve;
	
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
	    studentNames = FXCollections.observableArrayList ();
		for(Student student: students){
			studentNames.add(student.getName());
		}
		return studentNames;
	}
	
	public static TreeSet<Grade> getGrades()
	{
	    Debug.log("Accessing Server", "Curve pulled from server");
	    return curve.getGrades();
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
		
		curve = new Curve();
		curve.add(new Grade("A", 100, 90));
		curve.add(new Grade("B", 90, 80));
		curve.add(new Grade("C", 80, 70));
		curve.add(new Grade("D", 70, 60));
		curve.add(new Grade("F", 60, 0));
		Debug.log("Initializing Server", "Server curve populated");
	}
}
