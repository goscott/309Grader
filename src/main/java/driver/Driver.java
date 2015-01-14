package driver;

import roster.GradeBookTester;
import roster.GradedItem;
import roster.Roster;
import roster.Student;

public class Driver {
	private static Grader grader;
	
	public static void main(String[] args) {
		grader = Grader.get();
		test();
	}
	
	private static void test() {
		Roster roster = new Roster("CPE 309", "Winter 2015");
		Grader.addRoster(roster);
		Grader.setCurrentRoster(roster);
		Grader.addAssignment(new GradedItem("Test", "sfds"));
		grader.printClassList();
		GradeBookTester.main(null);
	}
}
