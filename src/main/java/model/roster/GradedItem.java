package model.roster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A grade-able assignment, and a category in the gradebook
 * @author Gavin Scott
 */
public class GradedItem implements Serializable {
	/**
     * generated serial ID
     */
    private static final long serialVersionUID = 8994764986930533573L;
    private String name;
	private String descr;
	private double score;
	private double maxScore;
	private ArrayList<GradedItem> children;
	private GradedItem parent;

	/**
	 * Creates a graded item with the given information. 
	 * The parent will be set to null, and the score will
	 * be set to zero.
	 * @param name the assignment's name
	 * @param descr the assignment's description
	 */
	public GradedItem(String name, String descr) {
		this(name, descr, null, 0, 100);
	}
	
	/**
	 * Creates a graded item with the given information. The
	 * parent will be set to zero.
	 * @param name The assignment's name
	 * @param descr The assignment's description
	 * @param score The assignment's score
	 */
	public GradedItem(String name, String descr, double score) {
		this(name, descr, null, score, 100);
	}

	/**
	 * Creates a graded item with the given information. The
	 * score will be set to zero
	 * @param name The assignment's name
	 * @param descr The assignment's description
	 * @param parent The assignment's parent assignment or
	 * category
	 */
	public GradedItem(String name, String descr, GradedItem parent) {
		this(name, descr, parent, 0, 100);
	}

	/**
	 * Creates a graded item with the given input
	 * @param name The assignment's name
	 * @param descr The assignment's description
	 * @param parent The assignment's parent or parent category
	 * @param score The assignment's score
	 */
	public GradedItem(String name, String descr, GradedItem parent, double score, double maxScore) {
		this.name = name;
		this.descr = descr;
		this.score = score;
		this.parent = parent;
		this.maxScore = maxScore;
		if(parent != null) {
			parent.addChild(this);
		}
		children = new ArrayList<GradedItem>();
	}

	/**
	 * Gets the graded item's name
	 * @return String the name of the assignment
	 */
	public String name() {
		return name;
	}

	/**
	 * Gets the graded item's description
	 * @return String the description of the
	 * assignment
	 */
	public String descr() {
		return descr;
	}

	/**
	 * Gets the assignment's score
	 * @return double the score of the assignment
	 */
	public double score() {
		return score;
	}
	
	/**
	 * Gets the assignment's maximum score
	 * @return double the max score of the assignment
	 */
	public double maxScore() {
		return maxScore;
	}

	/**
	 * Sets the assignment's score to a percentage
	 * of the assignment's maximum score
	 * @param percent the percent (90.0, etc)
	 */
	public void setPercentScore(double percent) {
		score = percent/100 * maxScore;
	}
	
	/**
	 * Sets the assignment's score to the
	 * given value
	 * @param sc The new score
	 */
	public void setScore(double sc) {
		score = sc;
	}
	
	/**
	 * Sets the assignment's maximum score
	 * @param sc the new max score
	 */
	public void setMaxScore(double sc) {
		maxScore = sc;
	}

	/**
	 * Adds another graded item to the assignment's
	 * list of children
	 * @param item The child assignment; such as a 
	 * specific question or a subcategory
	 */
	public void addChild(GradedItem item) {
		children.add(item);
	}

	/**
	 * Removes a child from the assignment and marks
	 * the child as parentless
	 * @param item the child assignment to be removed
	 */
	public void removeChild(GradedItem item) {
		if (children.contains(item)) {
			item.parent = null;
			children.remove(item);
		}
	}

	/**
	 * Checks if the assignment has a parent
	 * @return boolean true if the assignment
	 * has a parent
	 */
	public boolean hasParent() {
		return parent != null;
	}
	
	/**
	 * Gets the assignment's parent
	 * @return GradedItem the parent
	 */
	public GradedItem getParent() {
		return parent;
	}

	/**
	 * Sets another assignment as the parent of
	 * this GradedItem, and marks this assignment
	 * as a child of the new parent. It also
	 * removes itself from its old parent's list
	 * of children.
	 * @param newParent The new parent assignment
	 */
	public void setParent(GradedItem newParent) {
		parent.removeChild(this);
		parent = newParent;
		newParent.addChild(this);
	}

	/**
	 * Calculates a score based on the assignment's
	 * children.
	 * @return double the sum of this assignment's 
	 * score and all of its children's scores
	 */
	public double calcScore() {
		double sc = 0;
		for (GradedItem item : children) {
			sc += item.calcScore();
		}
		return sc;
	}

	/**
	 * Compares this assignmnt with another object
	 * for logical euquivalence.
	 * @param other The other Object
	 */
	public boolean equals(Object other) {
		if ((other != null) && (other instanceof GradedItem)) {
			GradedItem oth = (GradedItem) other;
			return oth.name().equals(name) && oth.descr().equals(descr)
					&& (oth.score() == score);
		}
		return false;
	}

	/**
	 * Creates and returns a copy of this GradedItem
	 * @return GradedItem the copy
	 */
	public GradedItem copy() {
		return new GradedItem(name, descr, parent, score, maxScore);
	}

	/**
	 * Saves the assignment
	 * @param assignments The lit of assignments
	 * @return A String representing the assignment, which 
	 * can be saved
	 */
	public static String Save(List<GradedItem> assignments) {
		String toReturn = "";
		char secret = 1;
		for (GradedItem item : assignments) {
			toReturn += "A" + secret;
			toReturn += item.name + secret + item.descr + secret + item.score()
					+ secret;
			toReturn += "\n";
		}
		return toReturn;
	}
}
