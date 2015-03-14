package testing.administration;

import static org.junit.Assert.*;
import model.administration.User;
import model.administration.UserTypes;

import org.junit.Test;

/**
* Class UserTestJUnit is the companion testing class for class User.
*      
* UserTestJUnit implements the following module test plan:
* 
*      Phase 1: Unit Test the constructor.
*      
*      Phase 2: Unit Test editType()
*      
*      Phase 3: Unit Test equals()
*      
*      Phase 4: Unit addPermission()
*      
*      Phase 5: Unit Test setPermission()
*      
*      Phase 6: Unit Test setPermissions()
*      
* @author Mason Stevenson
*/
public class UserTestJUnit {

    /**
     * Phase 1.
     */
    @Test
    public void testConstructor() {
        User user = new User("first", "last", "myID", "myPw", UserTypes.USER_STUDENT);
        
        assertEquals("first name is incorrect", "first", user.getfName());
        assertEquals("last name is incorrect", "last", user.getlName());
        assertEquals("user id is incorrect", "myID", user.getId());
        assertEquals("user password is incorrect", "myPw", user.getPassword());
        assertEquals("user type is incorrect", UserTypes.USER_STUDENT, user.getType());
    }
    
    /**
     * Phase 2
     */
    @Test
    public void testEditType() {
        User user = new User("first", "last", "myID", "myPw", UserTypes.USER_STUDENT);
        
        user.editType(UserTypes.USER_TA);
        assertEquals("User type was not to changed to TA", UserTypes.USER_TA, user.getType());
        
        user.editType('!');
        assertEquals("User type was to changed to an invalid type", UserTypes.USER_TA, user.getType());
    }
    
    /**
     * Phase 3
     */
    @Test
    public void testEquals() {
        User user1 = new User("first", "last", "myID", "myPw", UserTypes.USER_STUDENT);
        User user2 = new User("alfdkasj", "alsdfkj", "myID", "aldfkj", UserTypes.USER_ADMIN);
        User user3 = new User("first", "last", "adslfkj", "myPw", UserTypes.USER_STUDENT);
        
        assertEquals("users with the same id are not shown to be equal", true, user1.equals(user2));
        assertEquals("users with the different ids are shown to be equal", false, user1.equals(user3));
        assertEquals("a user is shown to be equal to a non User object", false, user1.equals("NOT A USER"));
    }
    
    /**
     * Phase 4
     */
    @Test
    public void testAddPermission() {
        
    }
    
    /**
     * Phase 5
     */
    @Test
    public void testSetPermission() {
        
    }
    
    /**
     * Phase 6
     */
    @Test
    public void setSetPermissions() {
        
    }
}
