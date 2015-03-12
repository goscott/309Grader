package testing.roster;

import static org.junit.Assert.*;
import model.administration.User;
import model.driver.Grader;
import model.roster.Roster;
import model.roster.Student;

import org.junit.Test;
/**
 *  @author ShelliCrispen
 *  Phase 1: Unit Test testConstructor()
 *      Tests that the constructor sets elements correctly.
 *      
 *  Phase 2: Unit Test
 *  
 *  Phase 3: Unit Test
 *  
 *  Phase 4: Unit Test
 *  
 *  Phase 5: Unit Test
 *  
 *  Phase 6: Unit Test
 *  
 *  Phase 7: Unit Test
 *  
 *  Phase 8: Unit Test
 *
 */
public class StudentTest {
    /**
     * @author Shelli Crispen
     */
    @Test
    public void testConstructor() {
        Roster roster = new Roster("name", "instructor", 1, "quarter", null, null);
        Student student = new Student("name", "00000", "19403278", "Softwhere Engeineering", false, 4, roster);
        Student studen2 = new Student("name", "123", "123", "", false, 10);
        //Grader.setUser(new User("name", "name", "00000", "00000", 'a'));
        assertEquals("name", student.getName());
        assertEquals("00000", student.getId());
                
        assertEquals("name", roster.courseName());
        assertEquals("instructor", roster.getInstructorId());
        assertEquals(1, roster.getSection());
        assertEquals("quarter", roster.getQuarter());
    }
    
    /**
     * @author Gavin Scott
     */
    @Test
    public void testCopy() {
    	Roster roster1 = new Roster("name", "instructor", 1, "quarter", null, null);
    	Roster roster2 = new Roster("name", "instructor", 1, "quarter", null, null);
        Student student = new Student("name", "00000", "19403278", "Softwhere Engeineering", false, 4);
        
        roster1.addStudent(student);
    	assertFalse(roster2.containsStudent(student.getId()));
    	assertTrue(roster1.containsStudent(student.getId()));
    	
    	student.setRoster(null);
    	student.setRoster(roster1);
    	student.copyTo(roster2);
    	assertTrue(roster1.containsStudent(student.getId()));
    	assertFalse(roster2.containsStudent(student.getId()));
    	student.setRoster(roster1);
    	student.setRoster(null);
    }
}
