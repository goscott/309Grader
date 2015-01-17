package model.driver;

import model.roster.AddAssignmentDialogController;
import model.roster.GradedItem;
import model.roster.Roster;
import model.roster.ScoreNode;
import model.roster.Student;

public class Driver {
	private static Grader grader;
	
	public static void main(String[] args) {
		grader = Grader.get();
		test();
	}
	
	public static void test() {
		Roster roster = new Roster("CPE 309", "Winter 2015");
		Grader.addRoster(roster);
		Grader.setCurrentRoster(roster);
		Grader.addAssignment(new GradedItem("Test", "sfds"));
		GradedItem test2 = new GradedItem("Midterms", "sfds");
		GradedItem test3 = new GradedItem("Midterm 1", "sfds", test2);
		GradedItem test4 = new GradedItem("Midterm 2", "sfds", test2);
		GradedItem test5 = new GradedItem("Midterm 3", "sfds", test2);
		Grader.addAssignment(test2);
		Grader.addAssignment(test3);
		Grader.addAssignment(test4);
		Grader.addAssignment(test5);
		
		Student bob = new Student("Bob", "123");
		
		Grader.addStudent(new Student("Bob", "123"));
		Grader.addScore(bob, "Midterm 1", 90);
		Grader.addScore(bob, "Midterm 2", 86);
		Grader.addScore(bob, "Midterm 3", 95);
	}
}
