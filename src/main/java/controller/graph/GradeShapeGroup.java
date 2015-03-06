package controller.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javafx.scene.Node;
import model.curve.Grade;
import model.driver.Grader;

public class GradeShapeGroup {
	private ArrayList<Node> grades;
	private static HashMap<String, Double> locations;
	
	public GradeShapeGroup() {
		grades = new ArrayList<Node>();
		
		for(Grade grade : Grader.getCurve().getGrades()) {
			grades.add(new GradeShape(Histogram.SQUARE_START, getStartingLocation(grade), grade));
		}
		locations = new HashMap<String, Double>();
	}
	
	public void add(GradeShape grade) {
		grades.add(grade);
	}
	
	public Collection<Node> get() {
		ArrayList<Node> list = new ArrayList<Node>();
		for(Node shape : grades) {
			list.addAll(((GradeShape)shape).get());
		}
		return list;
	}
	
	static void updateLocation(String grade, double y) {
		locations.put(grade, y);
	}
	
	static double getLocationAbove(String grade) {
		return locations.get(grade);
	}
	
	private double getStartingLocation(Grade grade) {
		return (Histogram.NUM_TICKS - grade.value()) * Histogram.BAR_WIDTH + Histogram.TOP_BUFFER - Histogram.BAR_WIDTH / 2;
	}
}
