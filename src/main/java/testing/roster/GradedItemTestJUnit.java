package testing.roster;

import static org.junit.Assert.*;
import model.roster.GradedItem;

import org.junit.Test;
/**
 * Graded Item testing class
 * @author Shelli Crispen
 * @author Gavin Scott
 *
 */
public class GradedItemTestJUnit
{

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

}
