package testing.roster;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Calendar;

import model.announcements.Announcement;
import model.curve.Curve;
import model.curve.Grade;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Exporter;
import model.roster.GradedItem;
import model.roster.Roster;
import model.roster.Student;
import model.server.Server;
import static org.junit.Assert.*;

import org.junit.Test;

public class RosterTest {

	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testConstructor() {
		Roster roster = new Roster("name", "instructor", 1, "quarter", Calendar.getInstance(),
				Calendar.getInstance());
		assertFalse(roster.getStartDate().equals(null));
		assertFalse(roster.getEndDate().equals(null));

		assertEquals("name", roster.courseName());
		assertEquals("instructor", roster.getInstructorId());
		assertEquals(1, roster.getSection());
		assertEquals("quarter", roster.getQuarter());
	}

	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testCalendarStrings() {
		Roster roster = new Roster("name", "instructor", 1, "quarter", null,
				null);
		Calendar startDate = Calendar.getInstance();
		String result = startDate.get(Calendar.MONTH) + "/";
		result += startDate.get(Calendar.DAY_OF_MONTH) + "/";
		result += startDate.get(Calendar.YEAR);

		assertEquals(result, roster.getStartDateString());
		assertEquals(result, roster.getEndDateString());
	}

	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testSetCurve() {
		Roster roster = new Roster("name", "instructor", 1, "quarter", null,
				null);
		assertEquals(new Curve(), roster.getCurve());
		Curve curve = new Curve();
		curve.add(new Grade("test", 90, Color.GREEN));
		roster.setCurve(curve);
		assertEquals(curve, roster.getCurve());
		roster.setCurve(null);
		assertEquals(curve, roster.getCurve());
	}

	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testAddDropStudent() {
		Roster roster = new Roster("name", "instructor", 1, "quarter", Calendar.getInstance(),
				Calendar.getInstance());
		Student student = new Student("name", "12345", "19403278",
				"Softwhere Engeineering", false, 4);
		Student student2 = new Student("name", "123456", "19403278",
				"Softwhere Engeineering", false, 4);
		roster.addAssignment(new GradedItem("test", "", 100, false));
		
		assertFalse(Server.getStudentsAssociatedWithRoster(roster).contains(
				student));
		assertFalse(Server.getStudentsAssociatedWithRoster(roster).contains(
				student2));

		assertEquals(0, roster.getStudents().size());
		roster.addStudent(null);
		assertEquals(0, roster.getStudents().size());
		roster.addStudent(student);
		assertEquals(1, roster.numStudents());
		roster.addStudent(student);
		assertEquals(1, roster.getStudents().size());
		roster.addStudent(student2);
		assertEquals(2, roster.getStudents().size());
		
		assertTrue(roster.containsStudent(student.getId()));
		
		roster.dropStudent(student2);
		assertEquals(1, roster.getStudents().size());
		roster.dropStudent(student2);
		assertEquals(1, roster.getStudents().size());
		roster.dropStudent(null);
		assertEquals(1, roster.getStudents().size());
		roster.dropStudent(student);
		assertEquals(0, roster.getStudents().size());
	}

	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testEquals() {
		Roster roster = new Roster("name", "instructor", 1, "quarter", null,
				null);
		Roster roster2 = new Roster("name", "instructor", 1, "quarter", null,
				null);
		Roster roster3 = new Roster("name2", "instructor2", 10, "quarter2",
				null, null);
		Roster roster4 = new Roster("name", "instructor2", 1, "quarter", null,
				null);

		Roster roster5 = new Roster("name2", "instructor", 1, "quarter", null,
				null);
		Roster roster6 = new Roster("name", "instructor", 10, "quarter", null,
				null);
		Roster roster7 = new Roster("name", "instructor", 1, "quarter2", null,
				null);

		assertTrue(roster.equals(roster));
		assertTrue(roster.equals(roster2));
		assertFalse(roster.equals(roster3));
		assertTrue(roster.equals(roster4));

		assertFalse(roster.equals(roster5));
		assertFalse(roster.equals(roster6));
		assertFalse(roster.equals(roster7));

		assertFalse(roster.equals(null));
		assertFalse(roster.equals(new Student("name", "12345", "19403278",
				"Softwhere Engeineering", false, 4)));
	}

	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testAddDropAssignment() {
		Roster roster = new Roster("TEST", "instructor", 1, "quarter", null, null);
		Grader.setCurrentRoster(roster);
		GradedItem item = new GradedItem("Test", "", 100, false);
		
		assertEquals(0, roster.getAssignments().size());
		assertEquals(null, roster.getAssignment(item.name()));
		
		roster.addAssignment(item);
		assertEquals(1, roster.getAssignments().size());
		roster.addAssignment(item);
		assertEquals(1, roster.getAssignments().size());
		roster.addAssignment(null);
		assertEquals(1, roster.getAssignments().size());
		assertTrue(roster.getAssignments().contains(item));
		roster.dropAssignment(item);
		assertFalse(roster.getAssignments().contains(item));
		assertEquals(0, roster.getAssignments().size());
		roster.dropAssignment(item);

		// parent
		GradedItem child1 = new GradedItem("1", "", 100, item, false);
		GradedItem child2 = new GradedItem("2", "", 100, item, false);
		roster.addAssignment(item);
		roster.addAssignment(child1);
		roster.addAssignment(child2);
		
		assertTrue(roster.getAssignment(child1.name()).equals(child1));
		assertTrue(roster.getAssignmentNameList().contains(child1.name()));
		assertFalse(roster.getAssignmentNameList().contains("sfdgdfsg"));
		
		assertEquals(3, roster.getAssignments().size());
		assertTrue(roster.getAssignments().contains(item));
		assertTrue(roster.getAssignments().contains(child1));
		assertTrue(roster.getAssignments().contains(child2));
		assertTrue(item.getChildren().contains(child1));
		assertTrue(item.getChildren().contains(child2));
		
		roster.dropAssignment(child1);
		roster.dropAssignment(null);
		
		assertEquals(2, roster.getAssignments().size());
		assertTrue(roster.getAssignments().contains(item));
		assertFalse(roster.getAssignments().contains(child1));
		assertTrue(roster.getAssignments().contains(child2));
		assertFalse(item.getChildren().contains(child1));
		assertTrue(item.getChildren().contains(child2));
		
		roster.dropAssignment(item);
		assertEquals(0, roster.getAssignments().size());
		assertFalse(roster.getAssignments().contains(item));
		assertFalse(roster.getAssignments().contains(child1));
		assertFalse(roster.getAssignments().contains(child2));
	}
	
	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testScores() {
		Roster roster = new Roster("TEST", "instructor", 1, "quarter", null, null);
		Grader.setCurrentRoster(roster);
		GradedItem item = new GradedItem("Test", "", 100, false);
		Student s1 = new Student("bob", "123", "123", "afs", false, 189);
		Student s2 = new Student("bob2", "1234", "1234", "fafs", false, 189);
		
		roster.addAssignment(item);
		roster.addScore(s1, item, new Double(100));
		roster.addScore(s1, new GradedItem("as", "", 100, false), new Double(10));
		roster.addStudent(s1);
		roster.addScore(s1, new GradedItem("as", "", 100, false), new Double(10));
		
		roster.addAssignment(new GradedItem("asdfss", "", 100, true));
		roster.addAssignment(item);
		roster.addStudent(s2);
		
		assertEquals(null, Grader.getScore(s1,  item.name()));
		assertEquals(null, Grader.getScore(s2,  item.name()));
		
		Grader.addScore(s1, item.name(), new Double(12.3));
		assertEquals(new Double(12.3), Grader.getScore(s1, item.name()));//(s1, item.name()));
		assertEquals(null, Grader.getScore(s2,  item.name()));
		
		roster.addScore(s1, item, new Double(-1));
		assertEquals(new Double(12.3),Grader.getScore(s1,  item.name()));
		assertEquals(null, Grader.getScore(s2,  item.name()));
		
		roster.addScore(s1, item, new Double(1265));
		assertEquals(new Double(12.3), Grader.getScore(s1,  item.name()));
		assertEquals(null, Grader.getScore(s2,  item.name()));
		
		roster.addScore(s1, item, null);
		roster.addScore(null, item, null);
		roster.addScore(null, new GradedItem("sadasda", "", 1, true), null);
		
		assertEquals(null, Grader.getScore(s1,  item.name()));
		assertEquals(null, Grader.getScore(s2,  item.name()));
		assertEquals(null, roster.getStudentGrade(null, ""));
		
		roster.addAssignment(new GradedItem("asdasd", "", 90, false));
		assertEquals(null, Grader.getScore(s2,  "asdasd"));
		
		Roster empty = new Roster("", "", 1, "", null, null);
		assertEquals(null, empty.getStudentGrade(s1, item.name()));
	}
	
	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testGetStudentsByScores() {
		Roster roster = new Roster("TEST", "instructor", 1, "quarter", null, null);
		Grader.setCurrentRoster(roster);
		GradedItem item = new GradedItem("Test", "", 100, false);
		Student s1 = new Student("bob", "123", "123", "afs", false, 189);
		Grader.addAssignment(item);
		Grader.addStudent(s1);
		
		assertEquals(null, s1.getGrade());
		
		Grader.addScore(s1, item.name(), new Double(100));
		
		assertEquals(new Double(100), Grader.getRoster().getStudentGrade(Grader.getStudentList().get(0), item.name()));		
		
		Roster roster2 = new Roster("TEST", "instructor", 1, "quarter", null, null);
		roster2.addStudent(s1);
		roster2.addAssignment(item);
		roster2.addAssignment(new GradedItem("asdasd", "", 100, true));
		roster2.addScore(s1, item, new Double(100));
		
		assertTrue(roster2.getLetterAverage().equals("A"));
		assertEquals(0, roster2.getFailingNum());
		assertEquals(1, roster2.getPassingNum());
		assertEquals(new Double(100.0), new Double(roster2.getMaxPoints()));
		
		assertEquals(new Double(0), new Double(roster2.getMaxPoints(s1)));
		assertEquals(1, roster2.getNumStudentsWithScore(100.0));
		assertEquals(0, roster2.getNumStudentsWithScore(0.0));
		assertEquals(new Double(100), new Double(roster2.getPercentAverage()));
		
		assertEquals(new Double(0), new Double(roster2.getTotalScore(s1)));
		assertEquals(new Double(0), new Double(roster2.getTotalScore(null)));
		roster2.addAssignment(new GradedItem("1", "", item, 100, false));
		assertEquals(new Double(0), new Double(roster2.getTotalScore(s1)));
		
		assertEquals(new Double(0), new Double(roster.getMaxPoints()));
		assertEquals(new Double(0), new Double(roster.getMaxPoints(s1)));
	}
	
	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testRosterSynch() {
		Server.init();
		Roster roster = new Roster("TEST", "instructor", 1, "quarter", null, null);
		GradedItem item = new GradedItem("Test", "", 100, false);
		Student s1 = new Student("bob", "11111", "123", "afs", false, 189);
		Student s2 = new Student("user0", "00000","19403278", "Softwhere Engeineering", false, 4);
		roster.addStudent(s1);
		roster.addStudent(s2);
		roster.addStudent(new Student("a", "!!!!", "!!!!", "", false, 35));
		roster.addAssignment(item);
		roster.addAssignment(new GradedItem("asdasd", "", 100, true));
		roster.addScore(s1, item, new Double(100));
		Server.addRosterToUser("123", roster);
		Server.addRosterToUser("22222", roster);
		assertNotEquals(null, roster.rosterSync(true));
		assertNotEquals(null, roster.rosterSync(false));
	}
	
	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testAnnouncements() {
		Roster roster = new Roster("TEST", "instructor", 1, "quarter", null, null);
		assertEquals(0, roster.getAnnouncements().size());
		Announcement ann = new Announcement("test", "", "");
		roster.addAnnouncement(ann);
		assertEquals(1, roster.getAnnouncements().size());
		assertTrue(roster.getAnnouncements().contains(ann));
	}
	
	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testArchive() {
		Roster roster = new Roster("TEST", "instructor", 1, "quarter", null, null);
		assertTrue(roster.current());
		roster.archive();
		assertFalse(roster.current());
	}
	
	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testSaveLoad() {
		Roster roster = new Roster("TEST", "", 1, "", null, null);
		Roster.saveTemp(roster);
		Roster roster2 = Roster.load("Rosters/" + Roster.TEMP_NAME + ".rost");
		assertEquals(roster, roster2);
		
		roster = new Roster(Roster.TEMP_NAME, "", 1, "", null, null);
		Roster.save(roster);
		roster2 = Roster.load("Rosters/" + Roster.TEMP_NAME + "-01.rost");
		assertEquals(roster, roster2);
		
		roster = Roster.load("TEST");
		assertEquals(null, roster);
		
		try {
			Files.deleteIfExists(FileSystems.getDefault().getPath("Rosters/" + Roster.TEMP_NAME + "-01.rost"));
		} catch (IOException ex) {
			Debug.log("Error", "Testing error");
		}
	}
}
