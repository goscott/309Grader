package model.driver;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.administration.User;
import model.curve.Curve;
import model.history.CourseHistory;
import model.roster.GradedItem;
import model.roster.Roster;
import model.roster.Student;

/**
 * Main class for the program. Holds references 
 * to each roster.
 * 
 * @author Gavin Scott
 */
public class Grader {
	private static Grader grader = new Grader();
	private static Roster currentRoster;
	private static ArrayList<Roster> classList;
	private static User user;
	private static CourseHistory history;

	/**
	 * Initiates a Grader
	 */
	private Grader() {
		currentRoster = null;
		classList = new ArrayList<Roster>();
	}

	/**
	 * Gets the instance of the Grader
	 * 
	 * @return Grader the Grader (singleton)
	 */
	public static Grader get() {
		return grader;
	}

	/**
	 * Sets the current user of the Grader tool
	 * @param newUser the new user
	 */
	public static void setUser(User newUser) {
		Debug.log("New User", newUser.getfName() + " " + newUser.getlName() + " logged in");
		user = newUser;
	}
	
	/**
	 * Gets the current user of the Grader tool
	 * @return User the current user
	 */
	public static User getUser() {
		Debug.log("Grader Accessed", "Current User checked");
		return user;
	}
	
	/**
	 * Sets the curve of the current roster
	 * @param curve the new curve
	 */
	public static void setCurve(Curve curve) {
		currentRoster.setCurve(curve);
	}
	
	/**
	 * Gets the curve of the current roster
	 * @return the current roster's curve
	 */
	public static Curve getCurve() {
		return currentRoster.getCurve();
	}
	
	/**
	 * Gets the currently seleected roster
	 * 
	 * @return Roster the current roster
	 */
	public static Roster getRoster() {
		return currentRoster;
	}

	/**
	 * Adds a new class to the class list, but does not change the currently
	 * selected roster
	 * 
	 * @param newRoster
	 *            the new roster
	 */
	public static void addRoster(Roster newRoster) {
		classList.add(newRoster);
		Debug.log("Grader model updated", "New roster registered");
	}

	/**
	 * Sets a new roster as the current roster, allowing the user to view and
	 * make changes to it
	 * 
	 * @param newRoster
	 *            the new roster
	 */
	public static void setCurrentRoster(Roster newRoster) {
		currentRoster = newRoster;
		Debug.log("Grader model updated", "Current roster changed");
	}

	/**
	 * Adds a student to the current roster
	 * 
	 * @param student
	 *            The student to be added to the roster
	 */
	public static void addStudent(Student student) {
		currentRoster.addStudent(student);
		Debug.log("Grader model updated", "Student added to current roster");
	}
	
	/**
	 * Checks if a student is enrolled in the currently
	 * seleted roster
	 * @param student the student in question
	 * @return boolean true if the student is enrolled
	 */
	public static boolean studentEnrolled(Student student) {
		return currentRoster.getStudentsByName().contains(student);
	}

	/**
	 * Adds an assigment to the current roster
	 * 
	 * @param item
	 *            the GradedItem that will be added to the roster
	 */
	public static void addAssignment(GradedItem item) {
		currentRoster.addAssignment(item);
		Debug.log("Grader model updated", "Assignment added to current roster");
	}
	
	/**
	 * Adds an assigment to the current roster
	 * 
	 * @param item
	 *            the GradedItem that will be added to the roster
	 */
	public static GradedItem getAssignment(String asgn) {
		Debug.log("Grader model accessed", "Assignment pulled from current roster");
		return currentRoster.getAssignment(asgn);
	}

	/**
	 * Changes a student's score on an assignment, in the currently selected
	 * roster
	 * 
	 * @param student
	 *            The student whose score will be changed
	 * @param asgn
	 *            The name of the assignment that is being scored
	 * @param score
	 *            The student's new score
	 */
	public static void addScore(Student student, String asgn, double score) {
		currentRoster.getStudentByID(student.getId()).setScore(asgn, score);
		Debug.log("Grader model updated", "Score added -> " + student.getName()
				+ " has score " + score + " on " + asgn + '.');
	}
	
	/**
	 * Changes a student's score on an assignment in the currently selected
	 * roster as a percentage
	 * 
	 * @param student
	 *            The student whose score will be changed
	 * @param asgn
	 *            The name of the assignment that is being scored
	 * @param score
	 *            The student's new score as a percantage of the maximum score
	 */
	public static void addPercentageScore(Student student, String asgn, double percent) {
		currentRoster.getStudentByID(student.getId()).setPercentScore(asgn, percent);
		Debug.log("Grader model updated", "Score added");
	}

	/**
	 * Gets a student's score for a particular assignment from the current
	 * roster
	 * 
	 * @param student
	 *            The student whose grade is being checked
	 * @param asgn
	 *            The name of the assignment being checked
	 * @return double the student's score on that assignment
	 */
	public static double getScore(Student student, String asgn) {
		Debug.log("Grader model accessed", "Student score retrieved");
		return currentRoster.getScore(student, asgn);
	}

	/**
	 * Gets an observable list of all the students in the current roster
	 * 
	 * @return ObservableList<Student> The students in the class that is
	 *         currently selected
	 */
	public static ObservableList<Student> getStudentList() {
		Debug.log("Grader model accessed", "Student list retrieved");
		ObservableList<Student> data = FXCollections.observableArrayList();
		for (Student s : currentRoster.getStudentsByName()) {
			data.add(s);
		}
		return data;
	}

	/**
	 * Gets an observable list of the assignment names for the current roster,
	 * useful for fxml.
	 * 
	 * @return ObservableList<String> The names of all of the assignments in the
	 *         current roster
	 */
	public static ObservableList<String> getAssignmentNameList() {
		Debug.log("Grader model accessed", "Assignment names retrieved");
		ObservableList<String> data = FXCollections.observableArrayList();
		for (GradedItem item : currentRoster.getAssignments()) {
			data.add(item.name());
		}
		return data;
	}

	/**
	 * Gets an observable list of the assignments for the current roster, useful
	 * for fxml.
	 * 
	 * @return ObservableList<GradedItem> All of the assignments in the current
	 *         roster
	 */
	public static ObservableList<GradedItem> getAssignmentList() {
		Debug.log("Grader model accessed", "Assignment list retrieved");
		ObservableList<GradedItem> data = FXCollections.observableArrayList();
		for (GradedItem item : currentRoster.getAssignments()) {
			data.add(item);
		}
		return data;
	}
}
