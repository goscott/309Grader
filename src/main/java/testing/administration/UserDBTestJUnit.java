package testing.administration;

import static org.junit.Assert.*;

import java.io.File;

import model.administration.User;
import model.administration.UserDB;
import model.administration.UserTypes;

import org.junit.Test;

/**
 * Class UserDBTestJUnit is the companion testing class for class UserDb.
 * 
 * The UserDB class keeps track of all currently users currently registered for GraderTool. It has several important methods:
 *      -loadUserDB()
 *      -updateDB()
 *      -addUser()
 *      -removeUser()
 *      -editUserType()
 *      -login()
 *      
 *  UserDB stores all user info in a file, users.udb. 
 *  The goal for testing UserDB is to make sure that any time the state of the UserDB changes, 
 *  this file gets updated, reflects the changes, and has no other data added or removed.
 *  
 *  UserDBTestJUnit implements the following module test plan:
 *  
 *      Phase 1: Unit Test the constructor.
 *          
 *  
 *      Phase 2: Unit Test loadUserDB()
 *          This private method gets called exactly once when the UserDB is constructed. 
 *          It takes the information stored in users.udb and populates the UserDB with these initial user objects. 
 *          Testing for this method should include making sure that the file is opened and closed properly, 
 *          and that all the data in the file is correctly loaded into the UserDB.
 *  
 *      Phase 3: Unit Test updateDB()
 *          This private method gets called every time the UserDB changes state. 
 *          It rewrites the users.udb file to reflect the userDB’s current state. 
 *          To test this method, we must make sure that all the data in the userDB 
 *          has been written to the file.
 *          
 *  
 *      Phase 4: Unit Test addUser()
 *          This method adds a new User object to the UserDB. 
 *          It then calls updateUserDB() to write the changes to the users.udb file. 
 *          It should add (one and only one) User object if the User is not already in the DB. 
 *          Users are identified by their unique userID. Tests should determine that the user was 
 *          added with the correct information.
 *  
 *      Phase 5: Unit Test removeUser()
 *          This method removes a new User object from the UserDB. 
 *          It then calls updateUserDB() to write the changes to the users.udb file. 
 *          It should remove one (and only one) User object if the User is in the DB. 
 *          Users are identified by their unique userID. Tests should determine that the correct user was removed.
 *  
 *      Phase 6: Unit Test editUserType()
 *          This method changes the permissions level for one user. 
 *          It then calls updateUserDB() to write the changes to the users.udb file.  
 *          It should change the permissions level of one (and only one) User object if the User is in the DB. 
 *          Users are identified by their unique userID. Tests should determine that the 
 *          correct user was edited with the correct permissions level.
 *  
 *      Phase 7: Unit Test login()
 *          This method authenticates a username + password combination to determine if it is in the database or not. 
 *          It returns the User object if the information is correct and null otherwise. Testing should make sure that 
 *          1) A user object is only returned if the username + password combination is correct. 2) The correct User 
 *          object is returned. 3) Otherwise null is returned.
 *  
 *      Phase 8: Repeat phases 1 through 5
 *      
 *      Phase 9: Stress test by adding and removing 10000 Users
 * 
 * @author Mason Stevenson
 */
public class UserDBTestJUnit {

    /**
     * Phase 1.
     */
    @Test
    public void testConstructor() {
        String directory = "LoginData";
        String fileName = "dbtest.udb";
        File file;
        
        UserDB database = new UserDB(fileName);
        file = new File(directory + "/" + fileName);
        
        assertEquals(directory + "/" + fileName + "does not exist", true, file.exists());
        
        file.delete();
    }
    
    /**
     * Phase 2/Phase 3
     */
    @Test
    public void testLoadUserDB() {
        String fileName = "dbtest.udb";
        String directory = "LoginData";
        File file;
        User user = new User("Bob", "McBob", "bmcb01", "12345", UserTypes.USER_STUDENT);
        
        UserDB database = new UserDB(fileName);
        database.addUser(user, true);
        
        database = null;
        database = new UserDB(fileName);
        
        assertEquals("User was not written to the database file", true, database.get(user.getId()) != null);
        assertEquals("User retrieved from database file is incorrect", true, database.get(user.getId()).equals(user));
        
        file = new File(directory + "/" + fileName);
        file.delete();
    }
    
    /**
     * Phase 4
     */
    @Test
    public void testAddUser() {
        String fileName = "dbtest.udb";
        String directory = "LoginData";
        File file;
        User user = new User("Bob", "McBob", "bmcb01", "12345", UserTypes.USER_STUDENT);
        
        UserDB database = new UserDB(fileName);
        database.addUser(user, true);
        
        assertEquals("User was not written to the database file", true, database.get(user.getId()) != null);
        assertEquals("User retrieved from database file is incorrect", true, database.get(user.getId()).equals(user));
        
        database.addUser(user, true);
        assertEquals("User was added to userDB twice", true, database.getUsers().size() == 1);
        
        file = new File(directory + "/" + fileName);
        file.delete();
    }
    
    /**
     * Phase 5.
     */
    @Test
    public void testRemoveUser() {
        String fileName = "dbtest.udb";
        String directory = "LoginData";
        File file;
        User user = new User("Bob", "McBob", "bmcb01", "12345", UserTypes.USER_STUDENT);
        
        UserDB database = new UserDB(fileName);
        database.addUser(user, true);
        assertEquals("User was not written to the database file", true, database.get(user.getId()) != null);
        
        database.removeUser(user);
        assertEquals("User was not removed from the database file", true, database.get(user.getId()) == null);
        
        database = null;
        database = new UserDB(fileName);
        assertEquals("User was not removed from the database file", true, database.get(user.getId()) == null);
        
        file = new File(directory + "/" + fileName);
        file.delete();
    }
    
    /**
     * Phase 6.
     */
    @Test
    public void testEditUserType() {
        String fileName = "dbtest.udb";
        String directory = "LoginData";
        File file;
        User user = new User("Bob", "McBob", "bmcb01", "12345", UserTypes.USER_STUDENT);
        
        UserDB database = new UserDB(fileName);
        database.addUser(user, true);
        assertEquals("User was not written to the database file", true, database.get(user.getId()) != null);
        
        database.editUserType(user, UserTypes.USER_INSTRUCTOR);
        assertEquals("User type was not changed", true, database.get(user.getId()).getType() == UserTypes.USER_INSTRUCTOR);
        
        database.editUserType(user, '@');
        assertEquals("User type was set to an invalid value", true, database.get(user.getId()).getType() == UserTypes.USER_INSTRUCTOR);
        
        file = new File(directory + "/" + fileName);
        file.delete();
    }
    
    /**
     * Phase 7.
     */
    @Test
    public void testLogin() {
        String fileName = "dbtest.udb";
        String directory = "LoginData";
        File file;
        User user = new User("Bob", "McBob", "bmcb01", "12345", UserTypes.USER_STUDENT);
        
        UserDB database = new UserDB(fileName);
        database.addUser(user, true);
        assertEquals("User was not written to the database file", true, database.get(user.getId()) != null);
        
        assertEquals("Login did not return the correct user object", true, database.login(user.getId(), user.getPassword()).equals(user));
        assertEquals("Login did not return when given an invalid password", true, database.login(user.getId(), "NOTMYPW") == null);
        assertEquals("Login did not return when given an invalid password", true, database.login("NOTMYUSERNAME", user.getPassword()) == null);
        
        file = new File(directory + "/" + fileName);
        file.delete();
    }
    
    /**
     * Phase 8.
     */
    @Test
    public void testRepeat() {
        testConstructor();
        testLoadUserDB();
        testAddUser();
        testRemoveUser();
        testEditUserType();
        testLogin();
    }
    
    /**
     * Phase 9.
     */
    /*
    @Test
    public void testAddRemove10000() {
        String fileName = "dbtest.udb";
        String directory = "LoginData";
        File file;
        int numToAdd = 10000;
        User user = new User("Bob", "McBob", "bmcb01", "12345", UserTypes.USER_STUDENT);
        
        UserDB database = new UserDB(fileName);
        
        for (int count = 0; count < numToAdd; count++) {
            database.addUser(new User("Bob", "McBob", "bmcb" + count, "12345", UserTypes.USER_STUDENT), true);
        }
        
        database = null;
        database = new UserDB(fileName);
        
        assertEquals("All " + numToAdd + " users were not loaded from the udb", true, database.getUsers().size() == numToAdd);
        
        for (int count = 0; count < numToAdd; count++) {
            database.removeUser(new User("Bob", "McBob", "bmcb" + count, "12345", UserTypes.USER_STUDENT));
        }
        
        assertEquals("Not all users were dropped from the udb", true, database.getUsers().size() == 0);
        
        database = null;
        database = new UserDB(fileName);
        
        assertEquals("Not all users were dropped from the udb", true, database.getUsers().size() == 0);
        
        file = new File(directory + "/" + fileName);
        file.delete();
    }
    */
}
