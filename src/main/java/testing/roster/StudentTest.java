package testing.roster;

import static org.junit.Assert.*;

import java.util.ArrayList;

import model.administration.User;
import model.driver.Grader;
import model.roster.GradedItem;
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
        
        assertFalse(student.equals(studen2));
        assertFalse(student.equals(null));
        assertFalse(student.equals("cat"));
        assertTrue(student.equals("00000"));
        assertFalse(student.equals(new Double(0)));
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
    
    /**
     * @author Gavin Scott
     */
    @Test
    public void testGetName() {
    	Student student = new Student("name", "00000", "19403278", "Softwhere Engeineering", false, 4);
    	assertEquals("name", student.getName());
    	Grader.setUser(new User("name", "name", "00000", "19403278", 's'));
    	assertEquals("name", student.getName());
    	Grader.setUser(new User("name", "name", "11111", "19403278", 's'));
    	assertEquals(Student.CENSORED, student.getName());
    	Grader.setUser(new User("name", "name", "11111", "19403278", 'a'));
    	assertEquals("name", student.getName());
    }
    
    /**
     * @author Gavin Scott
     */
    @Test
    public void testScore() {
    	Student stud = new Student("name", "00000", "19403278", "Softwhere Engeineering", false, 4);
    	stud.setRoster(null);
    	assertEquals(new Double(0), new Double(stud.getTotalScore()));
    	assertEquals(new Double(0), new Double(stud.getTotalPercentage()));
    	assertEquals(null, stud.getGrade());
    	assertEquals(null, stud.getGrade("sada"));
    	assertEquals(null, stud.getAssignmentScore("asda"));
    	stud.setScore("asd", new Double(100));
    	stud.setPercentScore("asd", 76);
    	
    	Roster rost = new Roster("1", "", 1, "", null, null);
    	GradedItem asgn = new GradedItem("name", "", 100, false);
    	rost.addAssignment(asgn);
    	rost.addStudent(stud);
    	stud.setRoster(rost);
    	
    	assertEquals(new Double(0), new Double(stud.getTotalScore()));
    	assertEquals(new Double(0), new Double(stud.getTotalPercentage()));
    	
    	asgn.setStudentScore(stud, new Double(100));
    	stud.setScore(asgn.name(), new Double(100));
    	assertEquals(new Double(100), new Double(stud.getTotalScore()));
    	assertEquals(new Double(100), new Double(stud.getTotalPercentage()));
    	assertEquals("A", stud.getGrade().getName());
    	assertEquals(new Double(100), stud.getAssignmentScore(asgn.name()));
    	assertEquals("A", stud.getGrade(asgn.name()).getName());
    	
    	stud.setPercentScore(asgn.name(), 76);
    	asgn.setStudentScore(stud, new Double(76));
    	assertEquals(new Double(76), stud.getAssignmentScore(asgn.name()));
    	
    	Student s2 = new Student("asddsda", "asd", "asd", "", false, 0);
    	assertEquals(s2.getName().compareTo(stud.getName()), s2.compareTo(stud));
    }
    
    /**
     * @author Gavin Scott
     */
    @Test
    public void testSave() {
    	Student st = new Student("asddsda", "qwe", "123", "zyx", false, 34);
    	ArrayList<Student> list = new ArrayList<Student>();
    	list.add(st);
    	assertEquals("Sasddsdaqwe\n", Student.Save(list));
    }
}
