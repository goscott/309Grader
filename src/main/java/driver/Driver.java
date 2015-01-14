package driver;

import roster.AddAssignmentDialog;
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
		Grader.addAssignment(new GradedItem("Test 2", "sfds"));
		Grader.addStudent(new Student("Bob", "123"));
		grader.printClassList();
		GradeBookTester.main(null);
		//AddAssignmentDialog.main(null);
	}
}
