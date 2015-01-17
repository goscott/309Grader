package model.administration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Mason Stevenson
 *
 */
public class UserDB {
    private ArrayList<User> users;
    private static final String DATABASE = "src/main/java/model/administration/users.udb";

    public UserDB() {
        loadUserDB();
        //print();
    }

    /**
     * Loads a list of users into the db.
     */
    private void loadUserDB() {
        File targetFile;
        BufferedReader reader;
        String line = "";
        String[] tokens;
        int index;

        users = new ArrayList<User>();

        targetFile = new File(DATABASE);

        try {

            // check if file exists
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }

            reader = new BufferedReader(new FileReader(targetFile));

            while ((line = reader.readLine()) != null) {
                tokens = line.split(",");
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
    private void updateDB() {
        BufferedWriter writer = null;
        File userData = new File(DATABASE);

        userData.delete();

        try {
            writer = new BufferedWriter(new FileWriter(DATABASE, true));
            
            for (User newUser : users) {
                // add the user to the db file
                writer.append(newUser.getfName() + "," + newUser.getlName()
                        + "," + newUser.getId() + "," + newUser.getPassword()
                        + "," + newUser.getType() + "\n");
            }
            
            writer.close();
        }
        
        catch (IOException e) {
            
        }
    }

    public boolean addUser(User newUser) {

        BufferedWriter writer = null;

        // check the db
        if (!users.contains(newUser)) {
            try {
                writer = new BufferedWriter(new FileWriter(DATABASE, true));

                // add the user to the db file
                writer.append(newUser.getfName() + "," + newUser.getlName()
                        + "," + newUser.getId() + "," + newUser.getPassword()
                        + "," + newUser.getType() + "\n");
                writer.close();

                // add the user to the db
                users.add(newUser);
            }

            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }

    public boolean removeUser(User target) {

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

    public boolean editUserType(User target, char newType) {
        // check if target exists
        if (users.contains(target)) {
            // edit User object type
            ((User) users.get(users.indexOf(target))).setType(newType);
            // edit User in udb
            updateDB();
            return true;
        }
        return false;
    }

    public Collection<User> getUsers() {
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
    public User get(String id) {
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
    public User login(String id, String password) {
        User temp = get(id);
        String filename = "src/main/java/model/administration/login.txt";
        File file;
        PrintWriter writer;
        
        //if id exists and password matches, return true        
        if (temp != null && temp.getPassword().equals(password)) {
            try {
                file = new File(filename);
                writer = new PrintWriter(file);
                writer.println(id);
                writer.close();
            }
            
            catch (Exception e) {
                e.printStackTrace();
            }
            
            return temp;
        }
        
        return null;
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
