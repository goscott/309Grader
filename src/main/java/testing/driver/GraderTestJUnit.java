package testing.driver;

import static org.junit.Assert.*;
import javafx.collections.ObservableList;
import model.administration.User;
import model.administration.UserDB;
import model.curve.Curve;
import model.curve.Grade;
import model.driver.Grader;
import model.roster.GradedItem;
import model.roster.Roster;
import model.roster.Student;

import org.junit.Test;

import resources.ResourceLoader;

/**
 * Grader Tests 
 * @author Shelli Crispen
 * @author Gavin Scott
 */
/**
 *  @author Shelli Crispen
 *  Phase 1: Unit Test testAddScore()
 *      Tests the addScore function and makes sure that a score was correctly 
 *      added to an assignment.
 *      
 *  Phase 2: Unit Test testGetSetUser()
 *      Tests that users are correctly set and can be retrieved correctly.
 *  
 *  Phase 3: Unit Test testGetUserDB()
 *      Tests that the userDB is returned correctly by the function.
 *      
 *  Phase 4: Unit Test testGetSetCurve()
 *      Tests that the Get and Set curve functions are working correctly. 
 *  
 *  Phase 5: Unit Test testAddAssignment() 
 *      Tests that the addAssignment function adds the assignment correctly 
 *      and the list size goes up by one. 
 *       
 *  Phase 6: Unit Test testGetAssignment() 
 *      Tests that an assignment can be retrieved by its name.  
 *     
 *  Phase 7: Unit Test testAddPercentageScore()
 *      Tests that the percentage score function can correctly add a percent to 
 *      an assignment for a specific student. 
 *      
 *  Phase 8: Unit Test testAddRoster()
 *      Tests the Roster (Not implemented yet)
 *      
 *  Phase 9: Unit Test testAddStudent()
 *      Tests that the Add Student function works correctly and the number of 
 *      students in the currentroster is incremented by 1. 
 */
public class GraderTestJUnit
{

    @Test
    public void testAddScore() {
        Grader.setCurrentRoster(new Roster("temp", "ins", 1, "fall", null, null));
        Student student1 = new Student("Bob", "12345", "19403278", "Softwhere Engeineering", false, 4);
        Grader.addStudent(student1);
        Grader.addAssignment(new GradedItem("temp1", "", 100, false));
        Grader.addAssignment(new GradedItem("temp2", "", 100, false));
        if (Grader.getScore(student1, "temp1") != null)
            fail("Scores initialized incorrectly : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", 50.0);
        if (Grader.getScore(student1, "temp1") != 50)
            fail("Score set incorrectly : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", 100.0);
        if (Grader.getScore(student1, "temp1") != 100)
            fail("Score set incorrectly (not accepting max score) : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", 0.0);
        if (Grader.getScore(student1, "temp1") != 0)
            fail("Score set incorrectly (not accepting zero) : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", 150.0);
        if (Grader.getScore(student1, "temp1") != 0)
            fail("Score accepting values above max score : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", -10.0);
        if (Grader.getScore(student1, "temp1") != 0)
            fail("Score accepting values below zero : " + Grader.getScore(student1, "temp1"));
        
        Grader.addScore(student1, "temp1", 57.2);
        if (Grader.getScore(student1, "temp1") != 57.2)
            fail("Score set incorrectly (not accepting decimals): " + Grader.getScore(student1, "temp1"));
        
        if (Grader.getScore(student1, "temp2") != null)
            fail("Unscored item no longer considered unchecked : " + Grader.getScore(student1, "temp2"));
        
    }
    
    @Test
    public void testGetSetUser() {
        User testU = new User("Rianna", "Uppal", "password", "password", 'a');
        Grader.setUser(testU);
        User temp = Grader.getUser();
        if(temp == null)
            fail("The User was not set.");
               
    }
    
    @Test 
    public void testGetUserDB()
    {
        UserDB temp = Grader.getUserDB();
        if(temp == null)
        {
            fail("The UserDB is null");
        }
    }

    @Test 
    public void testGetSetCurve(){
        Grader.setCurrentRoster(new Roster("temp", "ins", 1, "fall", null, null));
        Curve temp = null;
        Curve curve = new Curve();
        
        try{
            Grader.setCurve(curve);
            temp = Grader.getCurve();
        }
        catch(Exception ex)
        { 
            assertTrue(temp == null);   
        }
        
        assertTrue(temp == curve);
    }
    
    @Test
    public void testAddAssignment() {
        Grader.setCurrentRoster(new Roster("temp", "ins", 1, "fall", null, null));
        GradedItem grade = new GradedItem("", "", 50.0, true);
        
        Grader.addAssignment(grade);
        assertTrue(Grader.getAssignmentList().size() == 1);
        
        int temp = Grader.getAssignmentList().size();
        Grader.addAssignment(null);
        assertTrue(Grader.getAssignmentList().size() == temp);
    }
    
    @Test
    public void testGetAssignment(){
        Grader.setCurrentRoster(new Roster("temp", "ins", 1, "fall", null, null));
        GradedItem grade = new GradedItem("", "", 50.0, true);
        
        assertTrue(Grader.getAssignmentList().size() == 0);
        Grader.addAssignment(grade);
        
        assertTrue(Grader.getAssignmentList().size() == 1);
        GradedItem example = Grader.getAssignment("");
        assertTrue(example == grade);
    }

    @Test
    public void testAddPercentageScore() {
        Grader.setCurrentRoster(new Roster("temp", "ins", 1, "fall", null, null));
        Student student = new Student("Rianne", "12345", "19400000", "General Engeineering", false, 4);
        GradedItem grade = new GradedItem("assignment", "", 50.0, true);
        Grade info = new Grade("assignment", 50.0, ResourceLoader.GREEN);
        
        Grader.addStudent(student);
        
        assertTrue(Grader.getAssignmentList().size() == 0);
        Grader.addAssignment(grade);
        assertTrue(Grader.getAssignmentList().size() == 1);
        
        ObservableList<Student> temp = Grader.getStudentList();
        int index = temp.indexOf(student);
        
        assert(temp.get(index) == student);

        Grader.addPercentageScore(student, "assignment", 100);
        assert(temp.get(index).getGrade("assignement") == info);
        
    }

    @Test
    public void testAddRoster() {
    }

    @Test
    public void testAddStudent() {
        Grader.setCurrentRoster(new Roster("temp", "ins", 1, "fall", null, null));
        if (Grader.getStudentList().size() != 0)
            fail("Grader's list of students is not initialized correctly");

        Student student1 = new Student("Bob", "12345", "19403278", "Softwhere Engeineering", false, 4);
        Grader.addStudent(student1);
        if (Grader.getStudentList().size() != 1)
            fail("Incorrect number of students added");
    }

}
