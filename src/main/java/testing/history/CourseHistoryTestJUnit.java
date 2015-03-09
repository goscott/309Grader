package testing.history;

import static org.junit.Assert.*;

import java.util.Calendar;

import model.history.CourseHistory;
import model.roster.Roster;
import model.roster.Student;

import org.junit.Test;

/**
 * Class CourseHistoryTestJUnit is the companion testing class for class CourseHistory.
 * 
 * The CourseHistory class keeps track all Roster objects (different sections) for a single course that are no longer current. It has several important methods:
 *      -addRoster()
 *      -removeCourse()
 *      -hideCourse()
 *      -unhideCourse()
 *      
 * CourseHistoryTestJUnit implements the following module test plan:
 * 
 *      Phase 1: Unit Test the constructor. 
 *      
 *      Phase 2: Unit Test addRoster()
 *          addRoster adds a new roster to the history, if the roster does not already exist. 
 *          Testing for this method should check to make sure that only one roster was added, 
 *          only if is not already in the history.
 *      
 *      Phase 3: Unit Test removeCourse()
 *          RemoveCourse deletes a course from the history.
 *          It should remove one (and only one) Roster object if the roster is in history or hidden.
 *      
 *      Phase 4: Unit Test hideCourse()
 *          hideCourse moves a course from the history to a collection of roster objects called hidden. 
 *          It should only move the course if it exists in history.
 *      
 *      Phase 5: Unit Test unHideCourse()
 *          unhideCourse moves a course from hidden back to history. 
 *          It should only move the course if it exists in hidden.
 *          
 *      Phase 6: Repeat phases 1 through 5
 *      
 *      Phase 7: Stress test by adding and removing 1000 Rosters
 *      
 * @author Mason Stevenson
 */
public class CourseHistoryTestJUnit {

    /**
     * Phase 1.
     */
    @Test
    public void testConstructor() {
        String name = "Course 1";
        
        CourseHistory history = new CourseHistory(name);
        
        assertEquals("Course History is not empty following construction", true, history.getHistory().isEmpty());
        assertEquals("Course history name does not match: " + name, name, history.getCourseName());
        assertEquals("startYear should be initialized to -1", -1, history.getStartYear());
        assertEquals("endYear should be initialized to -1", -1, history.getEndYear());
        assertEquals("numSectionsTaught should be initialized to 0", 0, history.getNumSectionsTaught());
        assertEquals("totalStudents should be initialized to 0", 0, history.getTotalStudents());
    }
    
    /**
     * Phase 2.
     */
    @Test
    public void testAddRoster() {
        CourseHistory history = new CourseHistory("CSC 01");
        Roster roster = new Roster("CSC 01", "Bob", 1, "Spring", 
                Calendar.getInstance(), Calendar.getInstance());
        roster.addStudent(new Student("Student1", "1", "19403278", "Softwhere Engeineering", false, 4));
        
        history.addRoster(roster);
        assertEquals("Roster was added more than once.", 1, history.getHistory().size());
        assertEquals("Added roster does not equal retrieved roster.", true, roster.equals(history.getHistory().get(0)));
        assertEquals("numSectionsTaught was not incremented", 1, history.getNumSectionsTaught());
        assertEquals("totalStudents was not incremented", 1, history.getTotalStudents());
        
        history.addRoster(roster);
        assertEquals("Roster was added more than once.", 1, history.getHistory().size());
        assertEquals("numSectionsTaught is incorrect", 1, history.getNumSectionsTaught());
        assertEquals("totalStudents is incorrect", 1, history.getTotalStudents());
    }
    
    /**
     * Phase 3.
     */
    @Test
    public void testRemoveCourse() {
        CourseHistory history = new CourseHistory("CSC 01");
        Roster roster = new Roster("CSC 01", "Bob", 1, "Spring", 
                Calendar.getInstance(), null);
        
        history.addRoster(roster);
        history.removeCourse(roster);

        assertEquals("Roster was not removed.", 0, history.getHistory().size());
    }
    
    /**
     * Phase 4.
     */
    @Test
    public void testHideCourse() {
        CourseHistory history = new CourseHistory("CSC 01");
        Roster roster = new Roster("CSC 01", "Bob", 1, "Spring", 
                Calendar.getInstance(), null);
        
        history.addRoster(roster);
        history.hideCourse(roster);
        
        assertEquals("Roster was not moved from history to hidden", 0, history.getHistory().size());
        assertEquals("Roster did not appear in hidden", 1, history.getHidden().size());
        assertEquals("Roster does not equal the roster in history", true, history.getHidden().get(0).equals(roster));
    }
    
    /**
     * Phase 5.
     */
    @Test
    public void testUnHideCourse() {
        CourseHistory history = new CourseHistory("CSC 01");
        Roster roster = new Roster("CSC 01", "Bob", 1, "Spring", 
                Calendar.getInstance(), null);
        
        history.addRoster(roster);
        history.unHideCourse(roster);
        
        assertEquals("Roster did not appear in history", 1, history.getHistory().size());
        assertEquals("Roster was not moved from hidden to history", 0, history.getHidden().size());
        assertEquals("Roster does not equal the roster in history", true, history.getHistory().get(0).equals(roster));
    }
    
    /**
     * Phase 6.
     */
    @Test
    public void testRepeat() {
        testConstructor();
        testAddRoster();
        testRemoveCourse();
        testHideCourse();
        testUnHideCourse();
    }
    
    /**
     * Phase 7.
     */
    @Test
    public void testAddRemove1000() {
        CourseHistory history = new CourseHistory("CSC 01");
        int addAmount = 1000;
        
        
        for (int count = 0; count < addAmount; count++) {
            history.addRoster(new Roster("CSC 01", "Bob", count, "Spring", Calendar.getInstance(), null));
        }
        
        assertEquals("Not all 1000 rosters were added to history.", 1000, history.getHistory().size());
        
        for (int count = 0; count < addAmount; count++) {
            assertEquals("Not all 1000 rosters match", true, 
                    history.getHistory().get(count).equals(
                            new Roster(
                                    "CSC 01", "Bob", count, "Spring", Calendar.getInstance(), null)));
        }
    }

}
