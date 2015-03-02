package model.administration;

import java.util.ArrayList;

import model.driver.Debug;

/**
 * Holds login info for one user.
 * @author Mason Stevenson
 */
public class User {
    
    /**
     * First Name.
     */
    private String fName;
    
    /**
     * Last Name.
     */
    private String lName;
    
    /**
     * User login id (should be same as calpoly id).
     */
    private String id;
    
    /**
     * User password.
     */
    private String password;
    
    /**
     * User type. See model/administration/UserTypes.java
     */
    private char type;
    
    /**
     * Custom configuration of permission keys.
     */
    private ArrayList<PermissionKeys> permissions;

    /**
     * Constructor
     * @param newF A first name.
     * @param newL A last name.
     * @param newId The user's cal poly id.
     * @param newPw The user's password.
     * @param newType The user's permission level.
     */
    /*@
          ensures
          (
              fName.equals(newF) &&
              lName.equals(newL) &&
              id.equals(newId) &&
              password.equals(newPw) &&
              type == newType &&
              permissions == null
          );
     @*/
    public User(String newF, String newL, String newId, String newPw,
            char newType) {
        fName = newF;
        lName = newL;
        id = newId;
        password = newPw;
        type = newType;
        permissions = null;
    }

    /**
     * Modifies the user's permission type.
     * @param newType A new user type. (See UserTypes.java)
     * @return Returns true if the edit was successful.
     */
    /*@
          ensures
              UserTypes.isValidType(newType) ==> (type == newType && \result == true)
              
              &&
              
              !UserTypes.isValidType(newType) ==> \result == false;
     @*/
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
    /*@
        ensures (\result.equals(fName));
     @*/
    public String getfName() {
        return fName;
    }

    /**
     * Mutator for first name.
     */
    /*@
          requires (fName != null);
          
          ensures (this.fName.equals(fName));
     @*/
    public void setfName(String fName) {
        this.fName = fName;
    }

    /**
     * Accessor for last name.
     */
    /*@
        ensures (\result.equals(lName));
    @*/
    public String getlName() {
        return lName;
    }

    /**
     * Mutator for last name.
     */
    /*@
          requires (lName != null);
          ensures (this.lName.equals(lName));
     @*/
    public void setlName(String lName) {
        this.lName = lName;
    }

    /**
     * Accessor for id.
     */
    /*@
          ensures (\result.equals(id));
     @*/
    public String getId() {
        return id;
    }

    /**
     * Mutator for id.
     */
    /*@
          requires (id != null);
          
          ensures (this.id.equals(id));
     @*/
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Accessor for password.
     */
    /*@
          ensures (\result.equals(password));
     @*/
    public String getPassword() {
        return password;
    }

    /**
     * Mutator for password.
     */
    /*@
          requires (password != null && !password.equals(""));
          
          ensures (this.password.equals(password));
     @*/
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Accessor for type.
     */
    /*@
          ensures (\result == type);
     @*/
    public char getType() {
        return type;
    }

    /**
     * Mutator for type.
     */
    /*@
          requires (UserTypes.isValidType(type));
          
          ensures (this.type == type);
     @*/
    public void setType(char type) {
        this.type = type;
    }

    /**
     * Checks to see if two Users are equivalent. A user equals another if their ids match.
     * @param other Some other user.
     * @return Returns true if this user and other have matching ids.
     */
    /*@
            requires (other != null);
            
            ensures
            (
                (other.getId().equals(id)) ==> (\result == true)
                
                &&
                
                (!other.getId().equals(id)) ==> (\result == false)
            );
     */
    public boolean equals(Object other) {
        if (!(other instanceof User)) {
            return false;
        }
        
        if (((User)other).getId().equals(id)) {
            return true;
        }

        return false;
    }
    
    /**
     * Returns this user's custom permission key configuration, 
     * or the default configuration if this user does not have one set.
     */
    /*@
          ensures
          (
              (permissions == null) ==> (((ArrayList<PermissionKeys>)\result).equals(PermissionKeys.getKeys(type)))
              
              &&
              
              (permissions != null) ==> (((ArrayList<PermissionKeys>)\result).equals(permissions))
          );
     @*/
    public ArrayList<PermissionKeys> getPermissions() {
        
        if (permissions == null) {
            return PermissionKeys.getKeys(type);
        }
        
        else {
            return permissions;
        }
    }
    
    /**
     * Adds a new permission key to this user's list of permissions.
     */
    /*@
              ensures
              (
                  (\old(permissions) == null) ==> (permissions != null)
                  
                  &&
                  
                  (\old(permissions) != null) ==> (permissions.contains(key))
              );
     @*/
    public void addPermission(PermissionKeys key) {
        
        if (permissions == null) {
            permissions = new ArrayList<PermissionKeys>();
        }
        
        permissions.add(key);
    }
    
    public void setPermissions(ArrayList<PermissionKeys> list) {
        permissions = list;
    }
    
    /**
     * Removes key from this user's list of permissions.
     */
    /*@
          ensures
              (\old(permissions).contains(key)) ==> (!permissions.contains(key));
     @*/
    public void removePermission(PermissionKeys key) {
        
    }
}
