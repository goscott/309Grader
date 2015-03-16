package controller.graph;

import java.util.ArrayList;
import java.util.Collection;

import javafx.scene.Node;
import model.curve.Grade;
import model.driver.Grader;

/**
 * A group of gradeshapes. Displayed in a histogram.
 * @author Gavin Scott
 */
public class GradeShapeGroup {
	/** The GradeShapes in this group **/
	private ArrayList<Node> grades;
	
	/**
	 * Creates a group based on the current curve
	 */
	public GradeShapeGroup() {
		grades = new ArrayList<Node>();	
		for(Grade grade : Grader.getCurve().getGrades()) {
			if(!grade.getName().equals("F")) {
				grades.add(new GradeShape(getStartingLocation(grade), grade));
			}
		}
	}
	
	/**
	 * Adds a gradeshape to the group
	 */
	public void add(GradeShape grade) {
		grades.add(grade);
	}
	
	/**
	 * Gets a collection of all gradeshapes in the group.
	 * Used to add them all to the histogram.
	 */
	public Collection<Node> get() {
		ArrayList<Node> list = new ArrayList<Node>();
		for(Node shape : grades) {
			list.addAll(((GradeShape)shape).get());
		}
		return list;
	}
	
	/**
	 * Gets the starting y-coordinate of a gradeshape
	 * based on the grade
	 */
	private double getStartingLocation(Grade grade) {
		return Histogram.BAR_WIDTH + (Histogram.NUM_TICKS - grade.value()) * Histogram.BAR_WIDTH + Histogram.TOP_BUFFER - Histogram.BAR_WIDTH / 2 - 2*Histogram.BAR_WIDTH;
	}
}
