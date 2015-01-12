package roster;

public class Driver {
	private Roster roster;

	public static void main(String[] args) {
		Driver driver = new Driver();
		driver.startup();
	}

	public void startup() {
		System.out.println("Starting Calendar Tool...");
		roster = new Roster("CPE 309", "Winter 2015");
		test();
	}

	// TODO Delete
	private void test() {
		roster.addStudent(new Student("Gavin", "1111111"));
		roster.addStudent(new Student("Mason", "2222222"));
		roster.addStudent(new Student("Frank", "3333333"));
		roster.addStudent(new Student("Shelli", "4444444"));
		roster.addStudent(new Student("Jacob", "5555555"));
		roster.addStudent(new Student("Michael", "6666666"));
		roster.addAssignment(new GradedItem("Asgn1", "Blah blah blah"));
		roster.addAssignment(new GradedItem("Asgn2", "Blah blah blah"));
		roster.addAssignment(new GradedItem("Asgn3", "Blah blah blah"));
		roster.addScore(roster.getStudentByID("2222222"), new GradedItem(
				"Asgn2", "Blah blah blah"), new ScoreNode("total", 98.5));
		roster.print();
	}
}
