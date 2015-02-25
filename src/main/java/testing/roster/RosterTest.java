package testing.roster;

import model.roster.Roster;
import model.roster.Student;
import static org.junit.Assert.*;

import org.junit.Test;

public class RosterTest {
	
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
