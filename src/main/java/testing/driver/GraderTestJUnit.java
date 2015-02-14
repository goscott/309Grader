package testing.driver;

import static org.junit.Assert.*;
import model.driver.Grader;
import model.roster.GradedItem;
import model.roster.Roster;
import model.roster.Student;

import org.junit.Test;

/**
 * Grader Tests 
 * @author Shelli Crispen
 * @author Gavin Scott
 */
public class GraderTestJUnit
{

    @Test
    public void testAddScore() {
        Grader.setCurrentRoster(new Roster("temp", "ins", 1, "fall", null, null));
        Student student1 = new Student("Bob", "12345");
        Grader.addStudent(student1);
        Grader.addAssignment(new GradedItem("temp1", "", 100, false));
        Grader.addAssignment(new GradedItem("temp2", "", 100, false));
        if (Grader.getScore(student1, "temp1") != null)
            fail("Scores initialized incorrectly : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", 50);
        if (Grader.getScore(student1, "temp1") != 50)
            fail("Score set incorrectly : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", 100);
        if (Grader.getScore(student1, "temp1") != 100)
            fail("Score set incorrectly (not accepting max score) : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", 0);
        if (Grader.getScore(student1, "temp1") != 0)
            fail("Score set incorrectly (not accepting zero) : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", 150);
        if (Grader.getScore(student1, "temp1") != 0)
            fail("Score accepting values above max score : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", -10);
        if (Grader.getScore(student1, "temp1") != 0)
            fail("Score accepting values below zero : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", 57.2);
        if (Grader.getScore(student1, "temp1") != 57.2)
            fail("Score set incorrectly (not accepting decimals): " + Grader.getScore(student1, "temp1"));
        
        if (Grader.getScore(student1, "temp2") != null)
            fail("Unscored item no longer considered unchecked : " + Grader.getScore(student1, "temp2"));
        
    }

    @Test
    public void testAddAssignment() {
    }

    @Test
    public void testAddPercentageScore() {
    }

    @Test
    public void testAddRoster() {
    }

    @Test
    public void testAddStudent() {
        Grader.setCurrentRoster(new Roster("temp", "ins", 1, "fall", null, null));
        if (Grader.getStudentList().size() != 0)
            fail("Grader's list of students is not initialized correctly");

        Student student1 = new Student("Bob", "12345");
        Grader.addStudent(student1);
        if (Grader.getStudentList().size() != 1)
            fail("Incorrect number of students added");
    }

}
