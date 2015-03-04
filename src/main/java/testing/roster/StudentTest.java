package testing.roster;

import static org.junit.Assert.assertEquals;
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
        Student student = new Student("name", "00000", roster);

        assertEquals("name", student.getName());
        assertEquals("00000", student.getId());
                
        assertEquals("name", roster.courseName());
        assertEquals("instructor", roster.getInstructorId());
        assertEquals(1, roster.getSection());
        assertEquals("quarter", roster.getQuarter());
    }
    
    
}
