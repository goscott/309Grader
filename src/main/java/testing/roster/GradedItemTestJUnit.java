package testing.roster;

import static org.junit.Assert.*;
import model.roster.GradedItem;
import model.roster.Student;

import org.junit.Test;
/**
 * Graded Item testing class
 * @author Shelli Crispen
 * @author Gavin Scott
 *
 */
public class GradedItemTestJUnit
{

	/**
	 * @author Gavin Scott
	 */
    @Test
    public void testMaxScore()
    {
        GradedItem temp = new GradedItem("temp", "", 100, false);
        if(temp.maxScore() != 100)
            fail("Max score set incorrectly on initialization");
        temp.setMaxScore(-1);
        if(temp.maxScore() != 100)
            fail("Max score changed when given incorrect input " + "(-1)");
        temp.setMaxScore(-1.5);
        if(temp.maxScore() != 100)
            fail("Max score changed when given incorrect input " + "(-1.5)");
        temp.setMaxScore(0);
        if(temp.maxScore() != 0)
            fail("Max score not accepting correct input " + "(0)");
        temp.setMaxScore(0.5);
        if(temp.maxScore() != 0.5)
            fail("Max score not accepting correct input " + "(0.5)");
        temp.setMaxScore(1000);
        if(temp.maxScore() != 1000)
            fail("Max score not accepting correct input " + "(1000)");

    }
    
    /**
	 * @author Gavin Scott
	 */
    @Test
    public void testAddChild() {
        GradedItem parent1 = new GradedItem("parent1", "", 100, false);
        GradedItem parent2 = new GradedItem("parent2", "", 100, false);
        GradedItem child = new GradedItem("child", "", 100, parent1, false);
        
        if(parent1.getParent() != null)
            fail("GradedItems not initialized with null parents");
        
        if(parent2.getChildren().size() != 0)
            fail("Child list not being intialized to empty");
        
        if(!parent1.getChildren().contains(child))
            fail("Children aren't added to parents correctly on initialization");
        
        if(parent1.getChildren().size() != 1)
            fail("Initialized parent has the wrong number of children");
        
        if(!child.getParent().equals(parent1))
            fail("Parents aren't added to children correctly on initialization");
        
        parent2.addChild(child);
        if(!child.getParent().equals(parent2))
            fail("Child's parent is not being changed correctly");
        
        if(!parent2.getChildren().contains(child))
            fail("Parent's children are not being changed correctly");
        
        if(parent2.getChildren().size() != 1)
            fail("Parent's number of children is incorrect");
        
        if(parent1.getChildren().size() != 0)
            fail("Children not being removed from parents correctly");
    }

    /**
	 * @author Gavin Scott
	 */
    @Test
    public void testSetParent() {
    	GradedItem parent1 = new GradedItem("parent1", "", 100, false);
        GradedItem parent2 = new GradedItem("parent2", "", 100, false);
        GradedItem child = new GradedItem("child", "", 100, parent1, false);
        
        assertEquals(child.getParent(), parent1);
        assertTrue(parent1.getChildren().contains(child));
        assertFalse(parent2.getChildren().contains(child));
        
        child.setParent(parent2);
        
        assertEquals(child.getParent(), parent2);
        assertTrue(parent2.getChildren().contains(child));
        assertFalse(parent1.getChildren().contains(child));
        
        child.setParent(null);
        
        assertEquals(child.getParent(), null);
        assertFalse(parent1.getChildren().contains(child));
        assertFalse(parent2.getChildren().contains(child));
        
        child.setParent(parent1);
        
        assertEquals(child.getParent(), parent1);
        assertTrue(parent1.getChildren().contains(child));
        assertFalse(parent2.getChildren().contains(child));
    }
    
    /**
     * @author Gavin Scott
     */
    @Test
    public void testGetStudentScore() {
    	GradedItem grandparent = new GradedItem("parent1", "", 100, false);
        GradedItem parent1 = new GradedItem("child", "", 100, grandparent, false);
        GradedItem parent2 = new GradedItem("child", "", 100, grandparent, false);
        GradedItem parent3 = new GradedItem("child", "", 100, grandparent, false);
        GradedItem child1 = new GradedItem("child", "", 100, parent1, false);
        GradedItem child2 = new GradedItem("child", "", 100, parent1, false);
        GradedItem child3 = new GradedItem("child", "", 100, parent1, false);
        Student student = new Student("student", "12345", "19403278", "Softwhere Engeineering", false, 4);
        
        assertEquals(grandparent.getStudentScore(student), null);
        assertEquals(parent1.getStudentScore(student), null);
        assertEquals(parent2.getStudentScore(student), null);
        assertEquals(parent3.getStudentScore(student), null);
        assertEquals(child1.getStudentScore(student), null);
        assertEquals(child2.getStudentScore(student), null);
        assertEquals(child3.getStudentScore(student), null);
        
        child1.setStudentScore(student, 100.0);
        
        assertEquals(grandparent.getStudentScore(student), new Double(100));
        assertEquals(parent1.getStudentScore(student), new Double(100));
        assertEquals(parent2.getStudentScore(student), null);
        assertEquals(parent3.getStudentScore(student), null);
        assertEquals(child1.getStudentScore(student), new Double(100));
        assertEquals(child2.getStudentScore(student), null);
        assertEquals(child3.getStudentScore(student), null);
    }
    
    /**
     * @author Gavin Scott
     */
    @Test
    public void testSetStudentScore() {
    	GradedItem item = new GradedItem("child", "", 100, null, false);
        Student student = new Student("student", "12345", "19403278", "Softwhere Engeineering", false, 4);
        
        assertEquals(item.getStudentScore(student), null);
        
        item.setStudentScore(student, new Double(300));
        assertEquals(item.getStudentScore(student), null);
        
        item.setStudentScore(student, new Double(-300));
        assertEquals(item.getStudentScore(student), null);
        
        item.setStudentScore(student, new Double(100));
        assertEquals(item.getStudentScore(student), new Double(100));
        
        item.setStudentScore(student, new Double(0));
        assertEquals(item.getStudentScore(student), new Double(0));
        
        item.setStudentScore(student, new Double(45.87));
        assertEquals(item.getStudentScore(student), new Double(45.87));
    }
    
    /**
     * @author Gavin Scott
     */
    @Test
    public void testAddStudent() {
    	GradedItem item = new GradedItem("child", "", 100, null, false);
        Student student = new Student("student", "12345", "19403278", "Softwhere Engeineering", false, 4);
        Student student2 = new Student("student", "123456", "19403278", "Softwhere Engeineering", false, 4);
 
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(null, item.getStudentScore(student2));
        
        item.addStudent(null);
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(null, item.getStudentScore(student2));
        
        item.addStudent(student);
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(null, item.getStudentScore(student2));
        
        item.addStudent(student);
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(null, item.getStudentScore(student2));
        
        item.addStudent(student2);
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(null, item.getStudentScore(student2));
        
        item.addStudent(student);
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(null, item.getStudentScore(student2));
        
        item.addStudent(null);
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(null, item.getStudentScore(student2));
    }
    
    /**
     * @author Gavin Scott
     */
    @Test
    public void testRemoveStudent() {
    	GradedItem item = new GradedItem("child", "", 100, null, false);
        Student student = new Student("student", "12345", "19403278", "Softwhere Engeineering", false, 4);
        Student student2 = new Student("student", "123456", "19403278", "Softwhere Engeineering", false, 4);
        
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(null, item.getStudentScore(student2));
        
        item.removeStudent(student);
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(null, item.getStudentScore(student2));
        
        item.addStudent(student);
        item.addStudent(student2);
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(null, item.getStudentScore(student2));
        
        item.setStudentScore(null, 10.0);
        item.setStudentScore(student, 20.0);
        item.setStudentScore(student2, 30.0);
        assertEquals(null, item.getStudentScore(null));
        assertEquals(new Double(20), item.getStudentScore(student));
        assertEquals(new Double(30), item.getStudentScore(student2));
        
        item.removeStudent(student);
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(new Double(30), item.getStudentScore(student2));
        
        item.removeStudent(student2);
        assertEquals(null, item.getStudentScore(null));
        assertEquals(null, item.getStudentScore(student));
        assertEquals(null, item.getStudentScore(student2));
    }
    
    /**
     * @author Gavin Scott
     */
    @Test
    public void testEquals() {
    	GradedItem item = new GradedItem("test", "descr", 50, false);
    	GradedItem item2 = new GradedItem("test", "descr", 100, false);
    	GradedItem item3 = new GradedItem("test", "descr", 50, true);
    	GradedItem item4 = new GradedItem("test", "", 50, false);
    	GradedItem item5 = new GradedItem("test", "", 100, true);
    	
    	GradedItem item6 = new GradedItem("Test", "descr", 50, false);
    	Student student = new Student("name", "12345", "19403278", "Softwhere Engeineering", false, 4);
    	
    	assertEquals(item, item);
    	assertEquals(item, item2);
    	assertEquals(item, item3);
    	assertEquals(item, item4);
    	assertEquals(item, item5);
    	
    	assertFalse(item.equals(item6));
    	assertFalse(item.equals(student));
    	assertFalse(item.equals(null));
    }
}
