package model.server;

import java.util.ArrayList;

import model.roster.Student;

/**
 * A "Server" emulateing PolyLearn
 * @author Gavin Scott
 *
 */
public class Server {
	private static ArrayList<Student> students = new ArrayList<Student>();
	
	/**
	 * Gets the list of all students stored in the
	 * server
	 * @return ArrayList<Student> the list of students
	 */
	public static ArrayList<Student> getStudents() {
		return students;
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
	}
}
