package model.administration;

import model.driver.Debug;

/**
 * Holds login info for one user.
 * @author Mason Stevenson
 */
public class User {
    private String fName, lName, id, password;
    private char type;

    /**
     * Constructor
     * @param newF A first name.
     * @param newL A last name.
     * @param newId The user's cal poly id.
     * @param newPw The user's password.
     * @param newType The user's permission level.
     */
    public User(String newF, String newL, String newId, String newPw,
            char newType) {
        fName = newF;
        lName = newL;
        id = newId;
        password = newPw;
        type = newType;
    }

    /**
     * Modifies the user's permission type.
     * @param newType A new user type. (See UserTypes.java)
     * @return Returns true if the edit was successful.
     */
    public boolean editType(char newType) {
        Debug.log("model", "User.editType() invoked.");
        if (UserTypes.isValidType(newType)) {
            type = newType;
            return true;
        }
        return false;
    }

    /**
     * Accessor for first name.
     */
    public String getfName() {
        return fName;
    }

    /**
     * Mutator for first name.
     */
    public void setfName(String fName) {
        this.fName = fName;
    }

    /**
     * Accessor for last name.
     */
    public String getlName() {
        return lName;
    }

    /**
     * Mutator for last name.
     */
    public void setlName(String lName) {
        this.lName = lName;
    }

    /**
     * Accessor for id.
     */
    public String getId() {
        return id;
    }

    /**
     * Mutator for id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Accessor for password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Mutator for password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Accessor for type.
     */
    public char getType() {
        return type;
    }

    /**
     * Mutator for type.
     */
    public void setType(char type) {
        this.type = type;
    }

    /**
     * Checks to see if two Users are equivalent. A user equals another if their ids match.
     * @param other Some other user.
     * @return Returns true if this user and other have matching ids.
     */
    public boolean equals(User other) {

        if (other.getId().equals(id)) {
            return true;
        }

        return false;
    }
}
