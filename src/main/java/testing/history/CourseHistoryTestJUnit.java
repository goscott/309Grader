package testing.history;

import static org.junit.Assert.*;
import model.history.CourseHistory;

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
    }
    
    /**
     * Phase 2.
     */
    @Test
    public void testAddRoster() {
        fail("Not yet implemented");
    }
    
    /**
     * Phase 3.
     */
    @Test
    public void testRemoveCourse() {
        fail("Not yet implemented");
    }
    
    /**
     * Phase 4.
     */
    @Test
    public void testHideCourse() {
        fail("Not yet implemented");
    }
    
    /**
     * Phase 5.
     */
    @Test
    public void testUnHideCourse() {
        fail("Not yet implemented");
    }
    
    /**
     * Phase 6.
     */
    @Test
    public void testRepeat() {
        fail("Not yet implemented");
    }
    
    /**
     * Phase 7.
     */
    @Test
    public void testAddRemove1000() {
        fail("Not yet implemented");
    }

}
