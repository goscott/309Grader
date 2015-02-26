package model.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import controller.history.HistoryController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.administration.User;
import model.administration.UserDB;
import model.curve.Curve;
import model.history.HistoryDB;
import model.roster.GradedItem;
import model.roster.Roster;
import model.roster.Student;

/**
 * Main class for the program. Holds references to each roster.
 * 
 * @author Gavin Scott
 */
public class Grader {
	/** A singleton instance of the Grader **/
	private static Grader grader = new Grader();
	/** The current roster being viewed in the GUI **/
	private static Roster currentRoster;
	/** A list of all of the rosters contained in the Grader **/
	private static ArrayList<Roster> classList;
	/** The User that is currently logged in to the program **/
	private static User user;
	/** A database containing past rosters **/
	private static HistoryDB history = loadHistory();
	/** A database of all users **/
	private static UserDB userDB = new UserDB();

	private static HistoryController historyTab;

	/**
	 * Initiates a Grader
	 */
	/*@
    ensures(
        this.currentRoster == null;
        classList.equals(new ArrayList<Roster>());
    );
    @*/
	private Grader() {
		currentRoster = null;
		classList = new ArrayList<Roster>();
	}

	/**
	 * Gets the instance of the Grader
	 * 
	 * @return Grader the Grader (singleton)
	 */
	/*@
    ensures(
        \result.equals(grader)
    );
    @*/
	public static Grader get() {
		return grader;
	}

	/**
	 * Sets the current user of the Grader tool
	 * 
	 * @param newUser
	 *            the new user
	 */
	/*@
    ensures(
        \this.user.equals(newUser)
    );
    @*/
	public static void setUser(User newUser) {
		Debug.log("New User", newUser.getfName() + " " + newUser.getlName()
				+ " logged in");
		user = newUser;
	}

	/**
	 * Gets the current user of the Grader tool
	 * 
	 * @return User the current user
	 */
	/*@
    ensures(
        \result.equals(user)
    );
    @*/
	public static User getUser() {
		Debug.log("Grader Accessed", "Current User checked");
		return user;
	}

	/**
	 * Gets the user database.
	 * 
	 * @return Returns the static UserDB object held by this class.
	 */
	/*@
    ensures(
        \result.equals(userDB)
    );
    @*/
	public static UserDB getUserDB() {
		return userDB;
	}

	/**
	 * Sets a reference to the current history tab controller.
	 */
	/*@
    ensures(
        \this.historyTab.equals(newHC)
    );
    @*/
	public static void setHistoryController(HistoryController newHC) {
		historyTab = newHC;
	}

	/**
	 * Gets a reference to the current history tab controller.
	 */
	/*@
    ensures(
        \result.equals(historyTab)
    );
    @*/
	public static HistoryController getHistoryController() {
		return historyTab;
	}

	/**
	 * Gets the history database.
	 */
	/*@
    ensures(
        \result.equals(history)
    );
    @*/
	public static HistoryDB getHistoryDB() {
		// history = new HistoryDB();
		return history;
	}
	/*@
    ensures(
        \result.equals(History/data.hdb)
    );
    @*/
	public static HistoryDB loadHistory() {
		File database = new File("History/data.hdb");
		HistoryDB toReturn = null;

		if (database.exists()) {
			try {
				ObjectInputStream obj = new ObjectInputStream(
						new FileInputStream(database));
				toReturn = (HistoryDB) obj.readObject();
				obj.close();

			}

			catch (Exception e) {
				Debug.log("IO Error", e.toString());
				toReturn = new HistoryDB();
			}
		}

		else {
			toReturn = new HistoryDB();
		}

		return toReturn;
	}

	/**
	 * Sets the curve of the current roster
	 * 
	 * @param curve
	 *            the new curve
	 */
	/*@
    ensures(
        \this.currentRoster.curve.equals(curve)
    );
    @*/
	public static void setCurve(Curve curve) {
		currentRoster.setCurve(curve);
	}

	/**
	 * Gets the curve of the current roster
	 * 
	 * @return the current roster's curve
	 */
	/*@
    ensures(
        \result.equals(this.currentRoster.curve)
    );
    @*/
	public static Curve getCurve() {
		return currentRoster.getCurve();
	}

	/**
	 * Gets the currently seleected roster
	 * 
	 * @return Roster the current roster
	 */
	/*@
    ensures(
        \result.equals(currentRoster)
    );
    @*/
	public static Roster getRoster() {
		return currentRoster;
	}

	/**
	 * Gets the maximum points possible for all assignments in the current
	 * roster
	 * 
	 * @return double the max points
	 */
	/*@
    ensures(
        \result.equals(currentRoster.maxPoints)
    );
    @*/
	public static double getMaxPoints() {
		return currentRoster.getMaxPoints();
	}

	/**
	 * Adds a new class to the class list, but does not change the currently
	 * selected roster
	 * 
	 * @param newRoster
	 *            the new roster
	 */
	/*@
		requires(
			// The roster is valid
			newRoster != null
				&&
			// The roster is not already registered
			!classList.contains(newRoster)
		);
		ensures(
			// The new roster is the only change to the list of rosters
			\forall Roster other ;
				(classList.contains(other) <==>
					other.equals(newRoster) || \old(classList).contains(other))
		);
	@*/
	public static void addRoster(Roster newRoster) {
		// if(!classList.contains(newRoster)) {
		classList.add(newRoster);
		Debug.log("Grader model updated", "New roster registered");
		// }
	}

	/**
	 * Sets a new roster as the current roster, allowing the user to view and
	 * make changes to it
	 * 
	 * @param newRoster
	 *            the new roster
	 */
	/*@
    ensures(
        \currentRoster.equals(newRoster)
    );
    @*/
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
	/*@
	 	requires(
	 		student != null
	 			&&
	 		// The student is not already enrolled
	 		!currentRoster.containsStudent(student.getId())
	 			&&
	 		// The server contains a record of the student
			Server.getStudents().contains(student)
	 	);
	 	ensures(
	 		// The new student is the only change to the list of students enrolled
			\forall Student other ;
				(currentRoster.getStudents().contains(other) <==>
					other.equals(student) || \old(currentRoster.getStudents()).contains(other))
	 	);
	@*/
	public static void addStudent(Student student) {
		/*if(!currentRoster.containsStudent(student.getId()) &&
				Server.getStudents().contains(student))*/
		currentRoster.addStudent(student);
		Debug.log("Grader model updated", "Student added to current roster");
	}

	/**
	 * Checks if a student is enrolled in the currently seleted roster
	 * 
	 * @param student
	 *            the student in question
	 * @return boolean true if the student is enrolled
	 */
	public static boolean studentEnrolled(Student student) {
		return currentRoster.getStudents().contains(student);
	}

	/**
	 * Adds an assigment to the current roster
	 * 
	 * @param item
	 *            the GradedItem that will be added to the roster
	 */
	/*@
	 	requires(
	 		// The new item is not null
	 		item != null
	 			&&
	 		// The assignment is not already in the roster
	 		!currentRoster.getAssignments().contains(item)
	 	);
	 	ensures(
	 		// The new assignment is the only change to the list of assignments
	 		// in the roster
	 		(\forall GradedItem other ; 
	 			currentRoster.getAssignments().contains(other) <==>
	 				other.equals(item) || \old(currentRoster.getAssignments()).contains(other)
	 	);
	 @*/
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
		Debug.log("Grader model accessed",
				"Assignment pulled from current roster");
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
	/*@
	 	requires(
	 		// The assignment name references a valid assignment
	 		getAssignmentNameList().contains(asgn)
	 			&&
	 		// The score is valid
	 		score >= 0 && score <= getAssignment(asgn).maxScore()
	 			&&
	 		// The given student is valid and enrolled in the current roster
	 		currentRoster.getStudents().contains(student)
	 	);
	 	ensures(
	 		// The assignment's score has been set to the new value
	 		getAssignment(asgn).score() == score
	 	);
	 @*/
	public static void addScore(Student student, String asgn, double score) {
		// currentRoster.getStudentByID(student.getId()).setScore(asgn, score);
		currentRoster.addScore(student, getAssignment(asgn), score);
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
	/*@
	 	requires(
	 		// The assignment name references a valid assignment
	 		getAssignmentNameList().contains(asgn)
	 			&&
	 		// The percentage is valid
	 		percent >= 0 && percent <= 100
	 			&&
	 		// The given student is valid and enrolled in the current roster
	 		currentRoster.getStudents().contains(student)
	 	);
	 	ensures(
	 		// The assignment's score has been set to the new value
	 		getAssignment(asgn).score() == percent/100*getAssignment(asgn).maxScore()
	 	);
	 @*/
	public static void addPercentageScore(Student student, String asgn,
			double percent) {
		currentRoster.getStudentByID(student.getId()).setPercentScore(asgn,
				percent);
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
	public static Double getScore(Student student, String asgn) {
		Debug.log("Grader model accessed", "Student score retrieved");
		return currentRoster.getStudentByID(student.getId())
				.getAssignmentScore(asgn);
		// return currentRoster.getScore(student, asgn);
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
		if (currentRoster != null) {
			for (Student s : currentRoster.getStudents()) {
				data.add(s);
			}
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

	/**
	 * Loads a roster file from a file if it was not already loaded
	 */
	public static void importRoster(File file) {
		Debug.log("Import", "Attempting to load " + file.getName());
	}
}
