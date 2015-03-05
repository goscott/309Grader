package controller.graph;

import java.util.ArrayList;
import java.util.Collection;

public class GradeShapeGroup {
	private ArrayList<GradeShape> grades;
	
	public GradeShapeGroup() {
		grades = new ArrayList<GradeShape>();
	}
	
	public void add(GradeShape grade) {
		grades.add(grade);
		//Collections.sort(grades);
	}
	
	public Collection<GradeShape> get() {
		return grades;
	}
}
