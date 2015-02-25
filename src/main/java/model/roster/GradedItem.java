package model.roster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.driver.Debug;
import model.driver.Grader;

/**
 * A grade-able assignment, and a category in the gradebook
 * 
 * @author Gavin Scott
 */
public class GradedItem implements Serializable {
	/** Generated serial ID **/
	private static final long serialVersionUID = 8994764986930533573L;
	/** The name of the assignment **/
	private String name;
	/** The description of the assignment **/
	private String descr;
	/** The assignment's max score **/
	private double maxScore;
	/** The assignment's children **/
	private ArrayList<GradedItem> children;
	/** The assignment's parent **/
	private GradedItem parent;
	/** The assignment's depth **/
	// private int depth;
	/** Determines whether or not an assignment is extra credit **/
	private boolean extraCredit;
	/** Each student's grade on this assignment **/
	private HashMap<Student, Double> studentGrades;

	/**
	 * Creates a graded item with the given information. The parent will be set
	 * to null, and the score will be set to zero.
	 * 
	 * @param name
	 *            the assignment's name
	 * @param descr
	 *            the assignment's description
	 */
	public GradedItem(String name, String descr, double maxScore,
			boolean extraCredit) {
		this(name, descr, null, maxScore, extraCredit);
	}

	/**
	 * Creates a graded item with the given information. The score will be set
	 * to zero
	 * 
	 * @param name
	 *            The assignment's name
	 * @param descr
	 *            The assignment's description
	 * @param parent
	 *            The assignment's parent assignment or category
	 */
	public GradedItem(String name, String descr, double maxScore,
			GradedItem parent, boolean extraCredit) {
		this(name, descr, parent, maxScore, extraCredit);
	}

	/**
	 * Creates a graded item with the given input
	 * 
	 * @param name
	 *            The assignment's name
	 * @param descr
	 *            The assignment's description
	 * @param parent
	 *            The assignment's parent or parent category
	 * @param score
	 *            The assignment's score
	 */
	public GradedItem(String name, String descr, GradedItem parent,
			double maxScore, boolean extraCredit) {
		children = new ArrayList<GradedItem>();
		this.name = name;
		this.descr = descr;
		this.parent = parent;
		this.maxScore = maxScore;
		this.extraCredit = extraCredit;
		if (parent != null) {
			parent.addChild(this);
		}
		// depth = calcDepth();
		studentGrades = new HashMap<Student, Double>();
		for (Student student : Grader.getStudentList()) {
			studentGrades.put(student, null);
		}
	}

	/**
	 * Gets the graded item's name
	 * 
	 * @return String the name of the assignment
	 */
	public String name() {
		return name;
	}

	/**
	 * Gets the graded item's description
	 * 
	 * @return String the description of the assignment
	 */
	public String descr() {
		return descr;
	}

	/**
	 * Gets the assignment's maximum score
	 * 
	 * @return double the max score of the assignment
	 */
	public double maxScore() {
		return maxScore;
	}

	/**
	 * Sets the assignment's maximum score
	 * 
	 * @param sc
	 *            the new max score
	 */
	/*@
	 	requires( 
	  		sc >= 0 
	 	); 
	 	ensures( 
	 		maxScore == sc
	 	);
	 @*/
	public void setMaxScore(double sc) {
		if (sc >= 0) {
			maxScore = sc;
		}
	}

	/**
	 * Adds another graded item to the assignment's list of children
	 * 
	 * @param item
	 *            The child assignment; such as a specific question or a
	 *            subcategory
	 */
	/*@ 
	 	requires(
	 		item != null && !children.contains(item) 
	 	); 
	 	ensures(
	 		item.hasParent() => !item.getParent().getChildren().contains(item)
	 			&&
	 		item.getParent().equals(this) && 
	 		// The new child is the only change to the list of children
	 		(\forall GradedItem other ;
	 			children.contains(other) <==> other.equals(item)
	 				||
	 			\old(children).contains(other)
	 	);
	 @*/
	public void addChild(GradedItem item) {
		// TODO unattach old parent
		if (!children.contains(item)) {
			Debug.log("Child Added", item.name() + " added as a child of "
					+ name);
			for (Student student : studentGrades.keySet()) {
				studentGrades.put(student, null);
			}
			children.add(item);

			if (!item.getParent().equals(this)) {
				item.getParent().removeChild(item);
				item.setParent(this);
			}
		}
	}

	/**
	 * Returns the instance of the child with the given name
	 */
	/*@
	 	(\exists GradedItem child ; child.name().equals(name) 
	 		&& children.contains(child))
	 			==> child.equals(\result))
	 		||
	 	!(\exists GradedItem child ; child.name().equals(name) 
	 		&& children.contains(child))
	 			==> (\result == null)
	@*/
	public GradedItem getChild(String name) {
		for (GradedItem child : children) {
			if (child.name().equals(name)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Returns the number of children for this GradedItem
	 */
	public int numChildren() {
		return children.size();
	}

	/**
	 * Gets all the children of this graded item
	 */
	public ArrayList<GradedItem> getChildren() {
		return children;
	}

	/**
	 * Returns whether or not this assignment has any children
	 */
	public boolean hasChildren() {
		return children.isEmpty();
	}

	/**
	 * Removes a child from the assignment and marks the child as parentless
	 * 
	 * @param item
	 *            the child assignment to be removed
	 */
	public void removeChild(GradedItem item) {
		if (children.contains(item)) {
			item.parent = null;
			children.remove(item);
		}
	}

	/**
	 * Checks if the assignment has a parent
	 * 
	 * @return boolean true if the assignment has a parent
	 */
	public boolean hasParent() {
		return parent != null;
	}

	/**
	 * Gets the assignment's parent
	 * 
	 * @return GradedItem the parent
	 */
	public GradedItem getParent() {
		return parent;
	}

	/**
	 * Sets another assignment as the parent of this GradedItem, and marks this
	 * assignment as a child of the new parent. It also removes itself from its
	 * old parent's list of children.
	 */
	/*@
		ensures(
			newParent.getChildren().contains(this)
				&&
			parent.equals(newParent)
				&&
			!(\old(parent).getChildren().contains(this))
		);
	 @*/
	public void setParent(GradedItem newParent) {
		if (parent != null) {
			parent.removeChild(this);
		}
		parent = newParent;
		if (newParent != null) {
			newParent.addChild(this);
		}
	}

	/**
	 * Compares this assignmnt with another object for logical euquivalence.
	 * 
	 * @param other
	 *            The other Object
	 */
	public boolean equals(Object other) {
		if ((other != null) && (other instanceof GradedItem)) {
			GradedItem oth = (GradedItem) other;
			return oth.name().equals(name);
		}
		return false;
	}

	/**
	 * Creates and returns a copy of this GradedItem
	 * 
	 * @return GradedItem the copy
	 */
	public GradedItem copy() {
		return new GradedItem(name, descr, parent, maxScore, extraCredit);
	}

	/**
	 * Checks if the assignment is extra credit
	 * 
	 * @return boolean true if it is extra credit
	 */
	public boolean isExtraCredit() {
		return extraCredit;
	}

	/**
	 * Saves the assignment
	 * 
	 * @param assignments
	 *            The lit of assignments
	 * @return A String representing the assignment, which can be saved
	 */
	public static String Save(List<GradedItem> assignments) {
		String toReturn = "";
		char secret = 1;
		for (GradedItem item : assignments) {
			toReturn += "A" + secret;
			toReturn += item.name + secret + item.descr + secret;
			toReturn += "\n";
		}
		return toReturn;
	}

	/**
	 * Gives a String representation of a GradedItem
	 * 
	 * @return String the assignment as a String
	 */
	public String toString() {
		String ret = "Name: ";
		if (parent != null)
			ret += name + " | parent: " + parent.name() + " | ";
		else
			ret += name + " | parent: None | ";
		ret += " children:";
		for (GradedItem child : children) {
			ret += " " + child.name();
		}
		return ret;
	}

	/**
	 * Gets a student's grade on this assignment, calculated as a sum of all of
	 * the "leaves" underneath it in the assignment heirarchy
	 */
	// TODO WRITE COMPLICATED JML
	public Double getStudentScore(Student student) {
		if (student != null) {
			if (children.isEmpty()) {
				return studentGrades.get(student);
			} else {
				Double score = null;
				for (GradedItem child : children) {
					Double childGrade = child.getStudentScore(student);
					if (childGrade != null) {
						if (score == null) {
							score = childGrade;
						} else {
							score += childGrade;
						}
					}
				}
				return score;
			}
		} else {
			return null;
		}
	}

	/**
	 * Assigns a score on this assignment to a student
	 */
	/*@
	 	requires(
	 		children.isEmpty()
	 			&&
	 		sc >= 0 && sc <= maxScore
	 	);
	 	ensures(
	 		studentGrades.get(student).equals(sc)
	 	);
	 @*/
	public void setStudentScore(Student student, Double sc) {
		if (children.isEmpty() && sc <= maxScore && sc >= 0) {
			studentGrades.put(student, sc);
		}
	}

	/**
	 * Removes a student from the list of grades
	 */
	/*@
		requires(
			studentGrades.contains(student)
		);
		ensures(
			!studentGrades.contains(student)
		);
	@*/
	public void removeStudent(Student student) {
		if (studentGrades.containsKey(student)) {
			studentGrades.remove(student);
		}
	}

	/**
	 * Adds a student to the list of grades. Grade is initialized to null
	 */
	/*@
		requires(
			studentGrades.contains(student)
				&&
			student != null
		);
		ensures(
			!studentGrades.contains(student)
		);
	@*/
	public void addStudent(Student student) {
		if (student != null && !studentGrades.containsKey(student)) {
			studentGrades.remove(student);
		}
	}
}