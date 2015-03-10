package model.roster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.announcements.Announcement;
import model.curve.Curve;
import model.curve.Grade;
import model.driver.Debug;
import model.driver.Grader;
import model.server.Server;

/**
 * The class Roster that stores students and assignments
 * 
 * @author Gavin Scott
 * @author Shelli Crispen
 */
public class Roster implements Serializable {
	/** Generated serial ID **/
	private static final long serialVersionUID = -8021729176053193523L;
	/** The students enrolled in the class **/
	private ArrayList<Student> students;
	/** The assignments associated with the class **/
	private ArrayList<GradedItem> assignments;
	/** A map of student ids to the students in the class **/
	private HashMap<String, Student> ids;
	/** The class curve **/
	private Curve curve;
	/** Determines if a roster is archived **/
	private boolean current = true;

	/** The course's name **/
	private String courseName;
	/** The course's instructor **/
	private String instructorId;
	/** The course's section **/
	private int section;
	/** The course's quarter (or semester) **/
	private String quarter;
	/** The first date of the course **/
	private Calendar startDate = Calendar.getInstance();
	/** The last date of the course **/
	private Calendar endDate = Calendar.getInstance();
	/** The announcements associated with this roster **/
	private ArrayList<Announcement> announcements;
	/** The name for temporary roster **/
	public final static String TEMP_NAME = "TEMP_ROSTER";

	/**
	 * Contructs a roster from the given information. If startDate or endDate
	 * are null, they are set to the current day.
	 */
	/*@
		ensures(
			this.name.equals(name)
				&&
			this.instructor.equals(instructor)
				&&
			this.section == section
				&&
			this.quarter.equals(quarter)
				&&
			this.startDate.equals(startDate)
				&&
			this.endDate.equals(endDate)
				&&
			this.endDate != null
				&&
			this.startDate != null
		);
	@*/
	public Roster(String name, String instructorId, int section, String quarter,
			Calendar startDate, Calendar endDate) {
		courseName = name;
		this.instructorId = instructorId;
		this.section = section;
		this.quarter = quarter;

		if (startDate != null) {
			this.startDate = startDate;
		}
		if (endDate != null) {
			this.endDate = endDate;
		}

		students = new ArrayList<Student>();
		assignments = new ArrayList<GradedItem>();
		announcements = new ArrayList<Announcement>();
		ids = new HashMap<String, Student>();
		curve = new Curve();
	}

	/**
	 * Gets the course name
	 * 
	 * @return String the course name
	 */
	/*@
	 	ensures(
			((String)\result).equals(courseName)
		);
	@*/
	public String courseName() {
		return courseName;
	}

	/**
	 * Gets the course instructor
	 * 
	 * @return String the instructor
	 */
	/*@
	 	ensures(
			((String)\result).equals(instructor)
		);
	@*/
	public String getInstructorId() {
		return instructorId;
	}

	/**
	 * Gets the section of the class
	 * 
	 * @return int the section
	 */
	/*@
	 	ensures(
			((int)\result) == section
		);
	@*/
	public int getSection() {
		return section;
	}

	/**
	 * Gets the quarter the class takes place in
	 * 
	 * @return String the quarter
	 */
	/*@
	 	ensures(
			((String)\result).equals(quarter)
		);
	@*/
	public String getQuarter() {
		return quarter;
	}

	/**
	 * Gets the Date representing the first day of the class
	 * 
	 * @return Date the starting date
	 */
	/*@
	 	ensures(
			((Calendar)\result).equals(startDate)
		);
	@*/
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * returns a String representation of the start date.
	 */
	/*@
	 	ensures(
			((String)\result).equals(startDate.get(Calendar.MONTH) + "/" +
				startDate.get(Calendar.DAY_OF_MONTH) + "/" + 
				startDate.get(Calendar.YEAR))
		);
	@*/
	public String getStartDateString() {
		String result = "";

		result += startDate.get(Calendar.MONTH) + "/";
		result += startDate.get(Calendar.DAY_OF_MONTH) + "/";
		result += startDate.get(Calendar.YEAR);

		return result;
	}

	/**
	 * Gets the Date representing the last day of the class
	 * 
	 * @return Date the ending date
	 */
	/*@
	 	ensures(
			((Calendar)\result).equals(startDate)
		);
	@*/
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * returns a String representation of the start date.
	 */
	/*@
	 	ensures(
			((String)\result).equals(endDate.get(Calendar.MONTH) + "/" +
				endDate.get(Calendar.DAY_OF_MONTH) + "/" + 
				endDate.get(Calendar.YEAR))
		);
	@*/
	public String getEndDateString() {
		String result = "";

		result += endDate.get(Calendar.MONTH) + "/";
		result += endDate.get(Calendar.DAY_OF_MONTH) + "/";
		result += endDate.get(Calendar.YEAR);

		return result;
	}

	/**
	 * Sets the curve
	 * 
	 * @param curve
	 *            the new curve
	 */
	/*@
		requires(
			curve != null
		);
		ensures(
			this.curve.equals(curve)
		);
	@*/
	public void setCurve(Curve curve) {
		if (curve != null) {
			this.curve = curve;
		}
	}

	/**
	 * Gets the curve
	 * 
	 * @return the curve
	 */
	/*@
		ensures(
			((Curve)\result).equals(curve)
		);
	@*/
	public Curve getCurve() {
		return curve;
	}

	/**
	 * Adds a student to the course roster, giving the new student a default
	 * grade for every existing assignment in the roster
	 * 
	 * @param student
	 *            the new student
	 */
	/*@
		requires(
			student != null
				&&
			!students.contains(student)
		);
		ensures(
			students.contains(student)
				&&
			(\forall GradedItem item ; assignments.contains(item) ;
				item.getStudentScore(student) == null)
				&&
			Server.getStudentsAssociatedWithRoster(this).contains(student)
		);
	@*/
	public void addStudent(Student student) {
		if (student != null && !students.contains(student)) {
			Server.addRosterToUser(student.getId(), this);
			Student stud = student.copyTo(this);
			students.add(stud);
			ids.put(stud.getId(), stud);
			for (GradedItem item : assignments) {
				item.addStudent(stud);
			}
		}
	}

	/**
	 * Drops a student from the gradebook.
	 * 
	 * @param asgn
	 *            The GradedItem being added to the roster
	 */
	/*@
		requires(
			student != null
		);
		ensures(
			!students.contains(student)
				&&
			(\forall GradedItem item ; assignments.contains(item) ;
				item.getStudentScore(student) == null)
				&&
			!Server.getStudentsAssociatedWithRoster(this).contains(student)
		);
	@*/
	public void dropStudent(Student student) {
		if (student != null) {
			Server.removeRosterFromUser(student, this);
			students.remove(student);
			for (GradedItem item : assignments) {
				item.removeStudent(student);
			}
		}
	}

	/**
	 * Adds an assignment to the course
	 * 
	 * @param asgn
	 *            The GradedItem being added to the roster
	 */
	/*@
		requires(
			asgn != null
				&&
			!assignments.contains(asgn)
		);
		ensures(
			assignments.contains(asgn)
		);
	@*/
	public void addAssignment(GradedItem asgn) {
		if (asgn != null && !assignments.contains(asgn)) {
			assignments.add(asgn);
		}
	}

	/**
	 * Drop an assignment from the course
	 */
	/*@
		requires(
			asgn != null
				&&
			assignments.contains(asgn)
		);
		ensures(
			(\forall GradedItem item ; \old(asgn).getChildren().contains(item) ;
				!asgn.getChildren().contains(item))
				&&
			!assignments.contains(asgn)
				&&
			!assignments.get(assignments.indexOf(\old(asgn).getParent())).getChildren().contains(asgn)
		);
	@*/
	public void dropAssignment(GradedItem asgn) {
		if (asgn != null && assignments.contains(asgn)) {
			for (GradedItem child : asgn.getChildren()) {
				assignments.remove(child);
			}
			if (asgn.hasParent()) {
				assignments.get(assignments.indexOf(asgn.getParent()))
						.removeChild(asgn);
			}
			assignments.remove(asgn);
		}
	}

	/**
	 * Gets a reference to the assignment with the given name in the roster. If
	 * the assignment doesn't exist the returned value is null
	 */
	/*@
		ensures(
			((GradedItem)\result).name().equals(name)
		);
	@*/
	public GradedItem getAssignment(String name) {
		for(GradedItem item : assignments) {
			if(item.name().equals(name)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Scores a student for a particular assignment.
	 */
	/*@
		requires(
			students.contains(student)
				&&
			assignments.contains(asgn)
				&&
			score >= 0 && score <= asgn.maxScore()
		);
		ensures(
			asgn.getStudentScore(student) == score
				&&
			student.getAssignmentScore(asgn.name()) == score
		);
	@*/
	public void addScore(Student student, GradedItem asgn, Double score) {
		if (students.contains(student) && assignments.contains(asgn)
				&& (score == null || (score >= 0 && score <= asgn.maxScore()))) {
			Student stud = students.get(students.indexOf(student));
			asgn.setStudentScore(stud, score);
		}
	}

	/**
	 * Gets the score of a student on an assignment with the given name
	 */
	/*@
		requires(
			students.contains(student)
				&&
			assignments.contains(asgn)
		);
		ensures(
			((double)\result) == student.getAssignmentScore(asgn)
		);
	@*/
	public double getScore(Student student, String asgn) {
		if (students.contains(student) && assignments.contains(asgn)) {
			Student stud = students.get(students.indexOf(student));
			return stud.getAssignmentScore(asgn);
		}
		return -1;
	}

	/**
	 * Searches for a student by their ID
	 */
	/*@
		ensures(
			((Student)\result).equals(ids.get(id))
		);
	@*/
	public Student getStudentByID(String id) {
		return ids.get(id);
	}

	/**
	 * Checks if a student with the given ID exist in the roster
	 */
	/*@
		ensures(
			((boolean)\result) == students.contains(id)
		);
	@*/
	public boolean containsStudent(String id) {
		return students.contains(new Student("temp", id, null, null, false, 0));
	}

	/**
	 * Gets the number of students in the roster
	 */
	/*@
		ensures(
			((int)\result) == students.size()
		);
	@*/
	public int numStudents() {
		return students.size();
	}

	/**
	 * Gets all the students in the roster
	 */
	/*@
		ensures(
			(\forall Student student ; students.contains(student) ;
				((ArrayList<Student>)\result).contains(student))
		);
	@*/
	public ArrayList<Student> getStudents() {
		return students;
	}

	/**
	 * Gets all the students in the class with a given grade
	 */
	/*@
		ensures(
			(\forall Student student ; students.contains(student) && student.getGrade().equals(grade) ;
				((ArrayList<Student>)\result).contains(student))
		);
	@*/
	public ArrayList<Student> getStudentsByGrade(Grade grade) {
		ArrayList<Student> ret = new ArrayList<Student>();
		for (Student student : students) {
			if (student.getGrade().equals(grade)) {
				ret.add(student);
			}
		}
		return ret;
	}
	
	public int getNumStudentsWithScore(Double score) {
		int ret = 0;
		for (Student student : students) {
			if (student.getTotalPercentage() == score) {
				ret++;
			}
		}
		return ret;
	}

	/**
	 * Returns the number of students who are passing the course.
	 */
	/*@
		ensures(
			((int)\result) == getStudentsByGrade(new Grade("A", 90, 100)).size() +
				getStudentsByGrade(new Grade("B", 80, 90)).size() +
				getStudentsByGrade(new Grade("C", 70, 80)).size()
		);
	@*/
	public int getPassingNum() {
		int result = 0;

		for (Grade grade : getCurve().getGrades()) {
			if (grade.getName().equals("A") || grade.getName().equals("B")
					|| grade.getName().equals("C")) {
				result += getStudentsByGrade(grade).size();
			}
		}

		return result;
	}

	/**
	 * Returns the number of students who are failing the course.
	 */
	/*@
		ensures(
			((int)\result) == getStudentsByGrade(new Grade("A", 90, 100)).size() +
				getStudentsByGrade(new Grade("B", 80, 90)).size() +
				getStudentsByGrade(new Grade("C", 70, 80)).size()
		);
	@*/
	public int getFailingNum() {
		int result = 0;

		for (Grade grade : getCurve().getGrades()) {
			if (grade.getName().equals("D") || grade.getName().equals("F")) {
				result += getStudentsByGrade(grade).size();
			}
		}

		return result;
	}

	/**
	 * Checks this roster with an object for equality
	 */
	/*@
		ensures(
			((boolean)\result) == ((Roster)other).courseName().equals(courseName)
					&&
				((Roster)other).getSection() == section
					&&
				((Roster)other).getQuarter().equals(quarter)
		);
	@*/
	public boolean equals(Object other) {
		if ((other == null) || !(other instanceof Roster)) {
			return false;
		}
		Roster rost = (Roster) other;
		return rost.courseName().equals(courseName)
				&& rost.getSection() == section
				&& rost.getQuarter().equals(quarter);
	}

	/**
	 * Gets a list of all the assignments associated with this roster
	 * 
	 * @return The list of GradedItems
	 */
	/*@
		ensures(
			((ArrayList<GradedItem>)\result).equals(assignments)
		);
	@*/
	public ArrayList<GradedItem> getAssignments() {
		return assignments;
	}

	/**
	 * Returns a string representation of the roster
	 * 
	 * @return String the representation
	 */
	/*@
		ensures(
			((String)\result).equals(courseName + " " + section + " : " + instructor)
		);
	@*//*
	public String toString() {
		return courseName + " " + section + " : " + instructor;
	}*/

	/**
	 * Saves the roster to the computer
	 */
	/*@
		ensures(
			// saves the file
		);
	@*/
	public void Save() {
		save(this);
	}

	/**
	 * Saves a roster to the computer
	 * 
	 * @param rost
	 *            The roster to be saved
	 */
	/*@
		ensures(
			// saves the file
		);
	@*/
	public static void save(Roster rost) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("Rosters/" + rost.courseName + "-"
							+ String.format("%02d", rost.section) + ".rost"));
			out.writeObject(rost);
			out.close();
		} catch (IOException ex) {
			Debug.log("SAVE ERROR", "failed to save Roster " + rost.courseName);
			ex.printStackTrace();
		}
	}
	
	public static void saveTemp(Roster rost) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("Rosters/" + TEMP_NAME + ".rost"));
			out.writeObject(rost);
			out.close();
		} catch (IOException ex) {
			Debug.log("SAVE ERROR", "failed to save Roster " + rost.courseName);
			ex.printStackTrace();
		}
	}

	/**
	 * Loads a roster from the computer
	 * 
	 * @param url
	 *            the path to the roster
	 * @return Roster the loaded roster
	 */
	/*@
		ensures(
			// saves the file
		);
	@*/
	public static Roster load(String url) {
		Roster toReturn = null;
		try {
			FileInputStream in = new FileInputStream(url);
			ObjectInputStream obj = new ObjectInputStream(in);
			toReturn = (Roster) obj.readObject();
			obj.close();
			toReturn.setCurve(new Curve());

		} catch (FileNotFoundException e) {
			Debug.log(
					"IO ERROR",
					"Could not locate file at "
							+ url.substring(0, url.length() - 4));

		} catch (IOException e) {
			Debug.log(
					"IO ERROR",
					"Could not locate file at "
							+ url.substring(0, url.length() - 4)
							+ "(IOException)");
		} catch (ClassNotFoundException e) {
			Debug.log(
					"IO ERROR",
					"Could not locate file at "
							+ url.substring(0, url.length() - 4)
							+ "(Class Not Found)");
		}
		return toReturn;
	}

	/**
	 * Gets the maximum points possible for all assignments
	 * 
	 * @return double the max points
	 */
	/*@
		ensures(
			((double)\result) >= 0
		);
	@*/
	public double getMaxPoints() {
		double max = 0;
		for (GradedItem item : assignments) {
			if (!item.isExtraCredit() && item.isLeaf()) {
				max += item.maxScore();
			}
		}
		return max;
	}

	/**
	 * Returns the class average percentage grade.
	 * 
	 * @return the average percentage grade
	 */
	/*@
		ensures(
			((double)\result) >= 0
				&&
			((double)\result) <= 100
		);
	@*/
	public double getPercentAverage() {
		double sum = 0.0;
		int num = 0;
		for (Student student : students) {
			sum += student.getTotalPercentage();
			++num;
		}
		return sum / num;
	}

	/**
	 * Returns the class average letter grade.
	 * 
	 * @return the class average letter grade
	 */
	/*@
		ensures(
			((String)\result).equals(curve.get(getPercentAverage()).getName());
		);
	@*/
	public String getLetterAverage() {
		return curve.get(getPercentAverage()).getName();
	}

	/**
	 * Returns a student's grade on a particular assignment
	 */
	/*@
		requires(
			student != null
		);
		ensures(
			((Double)\result) >= 0
		);
	@*/
	public Double getStudentGrade(Student student, String asgn) {
		if (student != null) {
			for (GradedItem item : assignments) {
				if (item.name().equals(asgn)) {
					return item.getStudentScore(student);
				}
			}
		}
		return null;
	}

	/**
	 * Sets a student's grade on a particular assignment to the given score
	 */
	/*@
		requires(
			student != null
				&&
			sc != null
		);
		ensures(
			(\forall GradedItem item ; assignments.contains(item) && item.name().equals(asgn) ;
				item.getStudentScore(student).equals(sc))
		);
	@*/
	public void setStudentGrade(Student student, String asgn, Double sc) {
		if (student != null && sc != null) {
			for (GradedItem item : assignments) {
				if (item.name().equals(asgn)) {
					item.setStudentScore(student, sc);
				}
			}
		}
	}

	/**
	 * Gets a student's total score, as a sum of their scores on every
	 * assignment kept in the roster
	 */
	/*@
		requires(
			student != null
		);
		ensures(
			((double)\result) >= 0
		);
	@*/
	public double getTotalScore(Student student) {
		double total = 0;
		if (student != null) {
			for (GradedItem item : assignments) {
				if (item.isLeaf()) {
					total += item.getStudentScore(student) != null ? item
							.getStudentScore(student) : 0;
				}
			}
		}
		return total;
	}

	/**
	 * Checks if the roster is current or stored in history
	 */
	/*@
		ensures(
			((boolean)\result) == current
		);
	@*/
	public boolean current() {
		return current;
	}

	/**
	 * Sets the roster as no longer being current
	 */
	/*@
		ensures(
			current == false
		);
	@*/
	public void archive() {
	    File thisFile = new File("Rosters/" + courseName + "-" + String.format("%02d", section) + ".rost");
		current = false;
		Grader.getHistoryDB().addRoster(this);
		thisFile.delete();
		Save();
	}

	/**
	 * Returns a list of students that are not both in the roster and assoiated
	 * with the roster on the server side.
	 * 
	 * @param extraLocal
	 *            if extraLocal is true, the function returns a list of students
	 *            that are in the local roster and not in the server. If it is
	 *            false, it returns a list of students that are in the server
	 *            and not the local roster.
	 */
	/*@
		ensures(
			(extraLocal ==> 
				(\forall Student stud ; students.contains(stud)
					&& !Server.getStudentsAssociatedWithRoster(this).contains(stud) ;
						((Arraylist<Student>)\result).contains(stud)))
				&&
			(!extraLocal ==> 
				(\forall Student student ; !students.contains(student)
					&& Server.getStudentsAssociatedWithRoster(this).contains(student) ;
						((ArrayList<Student>)\result).contains(student)))
		);
	@*/
	public ArrayList<Student> rosterSynch(boolean extraLocal) {
		ArrayList<Student> list = new ArrayList<Student>();
		ArrayList<String> serverList = Server
				.getStudentsAssociatedWithRoster(this);
		if (extraLocal) {
			for (Student student : students) {
				if (!serverList.contains(student)) {
					list.add(student);
				}
			}
		} else {
			for (String id : serverList) {
				Student stud = Server.findUser(id);
				if (!students.contains(stud)) {
					list.add(stud);
				}
			}
		}
		return list;
	}

	/**
	 * Exports the roster
	 */
	/*@
	 	ensures(
	 		// the roster is exported
	 	);
	@*/
	public void export() {
		Exporter.exportRosterToExcel(this);
	}

	/**
	 * Gets an observable list of students
	 */
	public ObservableList<Student> getStudentList() {
		ObservableList<Student> data = FXCollections.observableArrayList();
		for (Student s : getStudents()) {
			data.add(s);
		}
		return data;
	}

	/**
	 * Gets an observable list of assignment names
	 */
	public ObservableList<String> getAssignmentNameList() {
		ObservableList<String> data = FXCollections.observableArrayList();
		for (GradedItem item : getAssignments()) {
			data.add(item.name());
		}
		return data;
	}

	/**
	 * Adds an announcement to a roster
	 */
	public void addAnnouncement(Announcement ann) {
		announcements.add(ann);
	}
	
	/**
	 * Adds an announcement to a roster
	 */
	public ArrayList<Announcement> getAnnouncements() {
		return announcements;
	}
}