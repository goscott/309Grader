package testing.server;

import static org.junit.Assert.*;
import javafx.collections.ObservableList;
import model.roster.Student;
import model.server.Server;

import org.junit.Before;
import org.junit.Test;
/**
 * Test JUint Class for the server. 
 * @author Shelli Crispen
 *
 */
public class TestSeverJUnit
{
    
    @Before
    public void setUp()
    {
        Server.init();
    }
    
    @Test
    public void test()
    {
       // fail("Not yet implemented");
    }

    @Test
    public void testGetObservableStudentList(){
        ObservableList<Student> testList = Server.getObservableStudentList();        
        assertTrue("Server does not have a list of students",testList != null);
    }
    
    @Test
    public void testGetStudentListNotRoster(){
        ObservableList<Student> testList =  Server.getStudentListNotRoster();        
        assertTrue("Server did not return a list of students not in roster", testList != null);
    }

    @Test
    public void testGetStudentListName() {
        ObservableList<String> testList =  Server.getStudentListName();        
        assertTrue("Server did not return a list of student names", testList != null);
    }
    
    @Test
    public void testGetStudentListNameNotRoster() {
        ObservableList<String> testList = Server.getStudentListNameNotRoster();
        assertTrue("Server did not return a list of student names not in roster", testList != null);
    }
}
