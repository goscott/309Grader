package testing.roster;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashMap;

import model.curve.Grade;
import model.driver.Grader;
import model.roster.GradedItem;
import model.roster.PredictionMath;
import model.roster.Roster;
import model.roster.Student;

import org.junit.Test;

public class PredictionMathTest {
	
	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testGetPrediction() {
		PredictionMath math = new PredictionMath();
		Roster roster = new Roster("", "", 1, "", null, null);
		Grader.setCurrentRoster(roster);
		GradedItem item1 = new GradedItem("1", "", 100, false);
		GradedItem item2 = new GradedItem("2", "", 100, false);
		Student student = new Student("bob", "1", "1", "", false, 1);
		Grade desiredGrade = new Grade("A", 90, Color.BLUE);
		Grader.addStudent(student);
		Grader.addAssignment(item1);
		
		HashMap<GradedItem, Double> map = PredictionMath.getPrediction(Grader.getRoster(), student, desiredGrade);
		assertEquals(new Double(90), map.get(item1));
		assertEquals(new Integer(1), new Integer(map.size()));
		
		Grader.addAssignment(item2);
		
		map = PredictionMath.getPrediction(Grader.getRoster(), student, desiredGrade);
		assertEquals(new Double(90), map.get(item1));
		assertEquals(new Double(90), map.get(item2));
		assertEquals(new Integer(2), new Integer(map.size()));
		
		GradedItem item3 = new GradedItem("3", "", 100, true);
		GradedItem item4 = new GradedItem("4", "", 100, false);
		GradedItem item5 = new GradedItem("5", "", 100, item4, false);
		GradedItem item6 = new GradedItem("6", "", 100, false);
		Grader.addAssignment(item3);
		Grader.addAssignment(item4);
		Grader.addAssignment(item5);
		Grader.addAssignment(item6);
		Grader.addScore(student, item6.name(), new Double(90));
		
		map = PredictionMath.getPrediction(Grader.getRoster(), student, desiredGrade);
		assertEquals(new Double(90), map.get(item1));
		assertEquals(new Double(90), map.get(item2));
		assertEquals(new Double(90), map.get(item5));
		assertEquals(new Integer(3), new Integer(map.size()));
		
		Grader.addScore(student, item1.name(), new Double(0));
		Grader.addScore(student, item2.name(), new Double(0));
		Grader.addScore(student, item3.name(), new Double(0));
		Grader.addScore(student, item4.name(), new Double(0));
		map = PredictionMath.getPrediction(Grader.getRoster(), student, desiredGrade);
		assertEquals(null, map);
	}
}
