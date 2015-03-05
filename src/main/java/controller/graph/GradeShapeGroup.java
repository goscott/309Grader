package controller.graph;

import java.awt.Color;
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
		int y = 20;
		
		for(Grade grade : Grader.getCurve().getGrades()) {
			grades.add(new GradeShape(100, y, grade));
			y += 20;
		}
		/*
		grades.add(new GradeShape(100, y, new Grade("A", 90, Color.GREEN)));
		y+=30;
		grades.add(new GradeShape(100, y, new Grade("B", 80, Color.CYAN)));
		y+=30;
		grades.add(new GradeShape(100, y, new Grade("C", 70, Color.YELLOW)));
		*/
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
}
