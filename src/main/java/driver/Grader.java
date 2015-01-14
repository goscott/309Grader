package driver;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import administration.UserDB;
import roster.GradedItem;
import roster.Roster;
import roster.Student;


/**
 * Main class (singleton) for the program.
 * Holds references to each roster (class)
 * 
 * @author Gavin Scott
 */
public class Grader {
	private static Grader grader = new Grader();
	private static Roster currentRoster;
	private static ArrayList<Roster> classList;
	
	private Grader() {
		currentRoster = null;
		classList = new ArrayList<Roster>();
	}
	
	public static Grader get() {
		return grader;
	}
	
	public static Roster getRoster() {
		return currentRoster;
	}
	
	public static void addRoster(Roster newRoster) {
		classList.add(newRoster);
	}
	
	public static void setCurrentRoster(Roster newRoster) {
		currentRoster = newRoster;
	}
	
	public static void addStudent(Student s) {
		currentRoster.addStudent(s);
		currentRoster.print();
	}
	
	public static void addAssignment(GradedItem item) {
		currentRoster.addAssignment(item);
		currentRoster.print();
	}
	
	/* For FXML stuff */
	public static ObservableList<Student> getStudentList() {
		ObservableList<Student> data = FXCollections
				.observableArrayList();
		for(Student s : currentRoster.getStudentsByName()) {
			data.add(s);
		}
		return data;
	}
	
	public void printClassList() {
		System.out.println("CLASS LIST");
		for(Roster r : classList) {
			System.out.println(r.toString());
		}
	}

}
