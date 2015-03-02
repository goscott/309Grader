package model.curve;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import model.driver.Debug;

/**
 * A representation of a course curve defined by percentage grades.
 * 
 * @author Frank Poole
 */
public class Curve implements Serializable {
	/** auto generated serial Version */
	private static final long serialVersionUID = 6753830198895233150L;
	/** Curve name designation */
	private String name;
	/** Set of grades that defines this curve */
	private ArrayList<Grade> curve;

	/**
	 * Returns an initialized curve.
	 */
	public Curve() {
		this.name = "";
		curve = new ArrayList<Grade>();
		curve.add(new Grade("A", 90, Color.GREEN));
		curve.add(new Grade("B", 80, Color.CYAN));
		curve.add(new Grade("C", 70, Color.YELLOW));
		curve.add(new Grade("D", 60, Color.ORANGE));
		curve.add(new Grade("F", 0, Color.RED));
	}

	/**
	 * Returns an empty curve with the given name designation.
	 * 
	 * @param name
	 *            the curve name designation
	 */
	public Curve(String name) {
		this.name = name;
		curve = new ArrayList<Grade>();
	}

	/**
	 * Add a unique grade to the curve. Grade percentage ranges must not overlap
	 * with existing grades.
	 * 
	 * @param grade
	 *            the grade to add
	 */
	/*
	 * @ requires !curve.contains(grade);
	 * 
	 * @
	 */
	public void add(Grade grade) {
		if (grade != null) {
			curve.add(grade);
		}
	}

	/**
	 * Returns the curve name designation.
	 * 
	 * @return the curve name designation
	 */
	/*
	 * @ ensures \result.equals(name);
	 * 
	 * @
	 */
	public String name() {
		return name;
	}

	/**
	 * Remove the given grade from this curve.
	 * 
	 * @param grade
	 *            the grade to remove
	 * @return false if not such grade is in the curve
	 */
	/*
	 * @ public normal_behavior requires curve.contains(grade); assignable
	 * \nothing; ensures !curve.contains(grade) && \result == true; also public
	 * normal_behavior assignable \nothing; ensures \result == false;
	 * 
	 * @
	 */
	public boolean remove(Grade grade) {
		if (grade != null) {
			return curve.remove(grade);
		}

		return false;
	}

	/**
	 * Reset the given grades percentage maximum and minimum.
	 * 
	 * @param grade
	 *            the grade to reset
	 * @param max
	 *            the new maximum percentage
	 * @param min
	 *            the new minimum percentage
	 */
	/*
	 * @ public normal_behavior requires max <= 100.0 && min >= 0.0 && max >
	 * min; also public normal_behavior requires !(max <= 100.0 && min >= 0.0 &&
	 * max > min);
	 * 
	 * @
	 */
	public void adjust(Grade grade, double value) {
		try {
			if (curve.contains(grade)) {
				if ((getGradeAbove(grade) != null && value <= getGradeAbove(
						grade).value())
						|| (getGradeAbove(grade) == null && value <= 100)
						|| (getGradeBelow(grade) != null && value >= getGradeBelow(
								grade).value())
						|| (getGradeBelow(grade) == null && value >= 0)) {
					curve.get(curve.indexOf(grade)).set(value);
				}
			}

			// curve.
		} catch (IllegalArgumentException e) {
			Debug.log("Grade Not Adjusted: Invalid Grade Range Argument(s)");
		}
	}

	/**
	 * Returns the set of grades that defines this curve.
	 * 
	 * @return the set of grades that defines this curve
	 */
	/*
	 * @ ensures \result.equals(curve);
	 * 
	 * @
	 */
	public ArrayList<Grade> getGrades() {
		Collections.sort(curve);
		return curve;
	}

	/**
	 * Returns the grade that represents the percentage score.
	 * 
	 * @param percentage
	 *            the percentage score
	 * @return the grade that represents the percentage score
	 */
	/*
	 * @ requires \forall grade != null;
	 * 
	 * @
	 */
	public Grade get(double percentage) {
		Grade ret = null;
		for (Grade grade : curve) {
			if (grade.contains(percentage)) {
				if(ret == null || grade.value() > ret.value()) {
					ret = grade;
				}
			}
		}
		return ret;
	}

	/**
	 * Returns a description of all grades currently defined in the curve.
	 */
	@Override
	public String toString() {
		String result = "";
		boolean first = true;

		for (Grade grade : curve) {

			if (!first) {
				result += " ";
			}

			result += (grade.getName() + " = " + grade.value());

			first = false;
		}

		return result;
	}

	/**
	 * Returns true if this curve contains all of the same grade names as
	 * another curve.
	 * 
	 * @param obj
	 *            another object being compared
	 * @return true if the object is equivalent to this curve
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}

		Curve other = (Curve) obj;

		for (Grade grade : curve) {
			if (!other.curve.contains(grade)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Gets the grade "above" the provided grade in the curve, if it exists.
	 * Returns null if the given grade is the highest in the curve or the
	 * provided grade is not in the current curve.
	 */
	public Grade getGradeAbove(Grade grade) {
		Collections.sort(curve);
		if (!curve.contains(grade)) {
			return null;
		}
		if (curve.indexOf(grade) == curve.size() - 1) {
			return null;
		}
		return curve.get(curve.indexOf(grade) + 1);
	}

	/**
	 * Gets the grade "above" the provided grade in the curve, if it exists.
	 * Returns null if the given grade is the highest in the curve or the
	 * provided grade is not in the current curve.
	 */
	public Grade getGradeBelow(Grade grade) {
		Collections.sort(curve);
		if (!curve.contains(grade)) {
			return null;
		}
		if (curve.indexOf(grade) == 0) {
			return null;
		}
		return curve.get(curve.indexOf(grade) - 1);
	}

	public Grade getGrade(String name) {
		Grade temp = new Grade(name, 100, null);
		if (curve.contains(temp)) {
			return curve.get(curve.indexOf(temp));
		}
		return null;
	}
}