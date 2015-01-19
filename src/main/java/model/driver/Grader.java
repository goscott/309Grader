package model.driver;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.administration.UserDB;
import model.roster.GradedItem;
import model.roster.Roster;
import model.roster.Student;


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
	}
	
	public static void addAssignment(GradedItem item) {
		currentRoster.addAssignment(item);
	}
	
	public static void addScore(Student student, String asgn, double score) {
		currentRoster.getStudentByID(student.getId()).setScore(asgn, score);
	}
	
	public static double getScore(Student student, String asgn) {
		return currentRoster.getScore(student, asgn);
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
	
	/**
	 * Gets an observable list of the assignment names for 
	 * the current roster, useful for fxml.
	 */
	public static ObservableList<String> getAssignmentNameList() {
		ObservableList<String> data = FXCollections
				.observableArrayList();
		for(GradedItem item : currentRoster.getAssignments()) {
			data.add(item.name());
		}
		return data;
	}
	
	/**
	 * Gets an observable list of the assignments for 
	 * the current roster, useful for fxml.
	 */
	public static ObservableList<GradedItem> getAssignmentList() {
		ObservableList<GradedItem> data = FXCollections
				.observableArrayList();
		for(GradedItem item : currentRoster.getAssignments()) {
			data.add(item);
		}
		return data;
	}
}
