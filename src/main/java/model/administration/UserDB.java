package model.administration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import model.driver.Debug;

/**
 * Holds login info for multiple users.
 * @author Mason Stevenson
 *
 */
public class UserDB {
    
    /**
     * A list of all registered users.
     */
    private ArrayList<User> users;
    
    /**
     * Relative (from project root) path to the file containing user info.
     */
    private static String DATABASE = "users.udb";
    
    /**
     * The path to the file containing the username of the last user who logged in.
     */
    private static final String LOGIN = "LoginData/login.txt";
    
    /**
     * Directory for login.txt.
     */
    private static final String DIR = "LoginData";
    
    /**
     * The string that delimits the items in the user info file.
     */
    private static final String DELIM = Character.toString((char) 0);

    /**
     * Constructor for UserDB. Calls loadUserDB(), which loads users from users.udb.
     */
    /*@
          ensures(users != null);
     @*/
    public UserDB() {
        Debug.log("model", "UserDB Created");
        loadUserDB();
    }
    
    /**
     * Constructor for UserDB. Sets a custom database filename.
     */
    /*@
          ensures
          (
              DATABASE == fileName &&
              users != null;
          );
     @*/
    public UserDB(String fileName) {
        Debug.log("model", "UserDB Created");
        DATABASE = fileName;
        loadUserDB();
    }

    /**
     * Loads a list of users into the db.
     */
    /*@
          ensures
              users != null;
     @*/
    private void loadUserDB() {
        File directory;
        File targetFile;
        BufferedReader reader;
        String line = "";
        String[] tokens;
        int index;

        users = new ArrayList<User>();

        directory = new File(DIR);
        targetFile = new File(DIR + "/" + DATABASE);

        try {
            
            if (!directory.exists()) {
                directory.mkdir();
            }
            
            // check if file exists
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile)));
            
            while ((line = reader.readLine()) != null) {
                tokens = line.split(DELIM);
                index = 0;
                users.add(new User(tokens[index++], tokens[index++],
                        tokens[index++], tokens[index++], tokens[index++]
                                .charAt(0)));
            }

            reader.close();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Overwrites the db file with the current list of users
     */
    /*@
          ensures
              (new UserDB()).getUsers().equals(users);
     @*/
    private void updateDB() {
        PrintWriter writer;

        try {
            writer = new PrintWriter(new File(DIR + "/" + DATABASE));
            
            for (User newUser : users) {
                // add the user to the db file
                writer.append(newUser.getfName() + DELIM + newUser.getlName()
                        + DELIM + newUser.getId() + DELIM + newUser.getPassword()
                        + DELIM + newUser.getType() + "\n");
            }
            
            writer.close();
        }
        
        catch (IOException e) {
            
        }
    }

    /**
     * Adds a user to the db.
     * @param newUser A user to add.
     * @return Returns true if the add was successful.
     */
    /*@
          requires
              newUser != null;
              
          ensures
              (!\old(users.contains(newUser)) ==> (users.contains(newUser));
     @*/
    public boolean addUser(User newUser, boolean doUpdate) {
        Debug.log("model", "UserDB.addUser() invoked.");

        // check the db
        if (!users.contains(newUser)) {
            
            users.add(newUser);
            
            //if do update if false, it wont overwrite the dbfile
            //this is helpful for testing.
            if (doUpdate) {
                updateDB();
                updateLogin(newUser.getId());
            }

            return true;
        }

        return false;
    }

    /**
     * Removes a user from the db.
     * @param target A user to remove.
     * @return Returns true if the remove was successful. 
     */
    /*@
          ensures
              (\old(users).contains(target)) ==> (!users.contains(target));
     @*/
    public boolean removeUser(User target) {
        Debug.log("model", "UserDB.removeUser() invoked.");
        
        // check to see if user exists
        if (users.contains(target)) {
            // remove from users
            users.remove(target);

            // remove from database
            updateDB();
            return true;
        }

        return false;
    }

    /**
     * Changes a user's permission type.
     * @param target A user to change.
     * @param newType A permission type. (See UserTypes.java)
     * @return Returns true if the change was successful.
     */
    /*@
          requires
              (UserTypes.isValidType(newType));
          
          ensures
              ((\result = true) => users.contains(target));
                  
     @*/
    public boolean editUserType(User target, char newType) {
        Debug.log("model", "UserDB.editUserType() invoked.");
        
        // check if target exists
        if (users.contains(target) && UserTypes.isValidType(newType)) {
            // edit User object type
            ((User) users.get(users.indexOf(target))).setType(newType);
            // edit User in udb
            updateDB();
            return true;
        }
        return false;
    }

    /** 
     * @return Returns the db.
     */
    /*@
          ensures(\result == users);
     @*/
    public Collection<User> getUsers() {
        Debug.log("model", "UserDB.getUsers() invoked.");
        return users;
    }

    /**
     * Returns a user with the corresponding user id. 
     * 
     * @param id
     *            The target id.
     * @return Returns a user, or null if the id does not match the one in the
     *         database.
     */
    /*@
          ensures
              (\exists User target ;
                  users.contains(target) ;
                      target.getId.equals(id) 
                      && \result == users.get(users.indexOf(target)))
               
               ||
               
               \result == null;
     @*/
    public User get(String id) {
        Debug.log("model", "UserDB.get() invoked.");
        for (User target : users) {
            if (target.getId().equals(id)) {
                return users.get(users.indexOf(target));
            }
        }

        return null;
    }
    
    /**
     * Fetches a matching User object if supplied with a valid user id and password.
     * @param id A user id.
     * @param password A user password.
     * @return Returns a User object, or null.
     */
    /*@
          ensures
              \exists User user;
                  Users.contains(user);
                      user.getId.equals(id)
                      && user.getPassword().equals(password) 
                      && \result == users.get(users.indexOf(target)))
              
              ||
              
              (\result == null);
     @*/
    public User login(String id, String password) {
        Debug.log("model", "UserDB.login() invoked.");
        
        User temp = get(id);
        
        
        //if id exists and password matches, return true        
        if (temp != null && temp.getPassword().equals(password)) {
            updateLogin(id);
            return temp;
        }
        
        return null;
    }
    
    /**
     * Writes the current user's id to login.txt. 
     * This will cause the login screen to remember the id
     * of the user who logged in last.
     */
    /*@
          requires 
              id != null
              
              &&
              
              !id.equals("");
     @*/
    private void updateLogin(String id) {
        File file;
        PrintWriter writer;
        
        try {
            //file = new File(this.getClass().getClassLoader().getResource(filename).getPath());
            file = new File(LOGIN);
            Debug.log("Writing to file" + file.getAbsolutePath());
            writer = new PrintWriter(file);
            writer.println(id);
            writer.close();
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Logs the current user out.
     * @return Returns true if the logout was successful.
     */
    /*@
          ensures
              \result == true;
     @*/
    public boolean logout() {
        Debug.log("model", "UserDB.logout() invoked");
        
        //set current user to null
        return true;
    }

    /**
     * Prints the user database to system.out
     */
    public void print() {

        System.out.println("USER DB:");
        System.out
                .println("----------------------------------------------------------------------------------------");
        System.out.printf("%-20s%-20s%-20s%-20s%-20s\n\n", "First", "Last",
                "ID", "Password", "Type");

        for (User u : users) {
            System.out.printf("%-20s%-20s%-20s%-20s%-20c\n", u.getfName(),
                    u.getlName(), u.getId(), u.getPassword(), u.getType());
        }
        System.out
                .println("----------------------------------------------------------------------------------------\n\n");
    }
}
