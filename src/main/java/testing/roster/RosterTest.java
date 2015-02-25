package testing.roster;

import java.util.Calendar;

import model.curve.Curve;
import model.curve.Grade;
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
		Roster roster = new Roster("name", "instructor", 1, "quarter", null, null);
		//assertEquals(roster.getStartDate(), Calendar.getInstance());
		//assertEquals(roster.getEndDate(), Calendar.getInstance());
		
		assertEquals("name", roster.courseName());
		assertEquals("instructor", roster.getInstructor());
		assertEquals(1, roster.getSection());
		assertEquals("quarter", roster.getQuarter());
	}
	
	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testCalendarStrings() {
		Roster roster = new Roster("name", "instructor", 1, "quarter", null, null);
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
	/*@Test
	public void testSetCurve() {
		Roster roster = new Roster("name", "instructor", 1, "quarter", null, null);
		assertEquals(new Curve(), roster.getCurve());
		Curve curve = new Curve();
		curve.add(new Grade("test", 90, 95));
		roster.setCurve(curve);
		assertEquals(curve, roster.getCurve());
		roster.setCurve(null);
		assertEquals(curve, roster.getCurve());
	}*/
	
	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testAddStudent() {
		Roster roster = new Roster("name", "instructor", 1, "quarter", null, null);
		Student student = new Student("name", "12345");
		Student student2 = new Student("name", "123456");
		
		assertFalse(Server.getAssociatedAnnouncements(roster).contains(student));
		assertFalse(Server.getAssociatedAnnouncements(roster).contains(student2));
		
		assertEquals(0, roster.getStudents().size());
		roster.addStudent(null);
		assertEquals(0, roster.getStudents().size());
		roster.addStudent(student);
		assertEquals(1, roster.getStudents().size());
		roster.addStudent(student);
		assertEquals(1, roster.getStudents().size());
		roster.addStudent(student2);
		assertEquals(2, roster.getStudents().size());
		
		//assertTrue(Server.getAssociatedAnnouncements(roster).contains(student));
		//assertTrue(Server.getAssociatedAnnouncements(roster).contains(student2));
	}
	
	/**
	 * @author Gavin Scott
	 */
	@Test
	public void testEquals() {
		Roster roster = new Roster("name", "instructor", 1, "quarter", null, null);
		Roster roster2 = new Roster("name", "instructor", 1, "quarter", null, null);
		Roster roster3 = new Roster("name2", "instructor2", 10, "quarter2", null, null);
		Roster roster4 = new Roster("name", "instructor2", 1, "quarter", null, null);
		
		Roster roster5 = new Roster("name2", "instructor", 1, "quarter", null, null);
		Roster roster6 = new Roster("name", "instructor", 10, "quarter", null, null);
		Roster roster7 = new Roster("name", "instructor", 1, "quarter2", null, null);
		
		assertTrue(roster.equals(roster));
		assertTrue(roster.equals(roster2));
		assertFalse(roster.equals(roster3));
		assertTrue(roster.equals(roster4));
		
		assertFalse(roster.equals(roster5));
		assertFalse(roster.equals(roster6));
		assertFalse(roster.equals(roster7));
		
		assertFalse(roster.equals(null));
		assertFalse(roster.equals(new Student("name", "12345")));
	}
}
