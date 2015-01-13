package administration;

/**
 * 
 * @author Mason Stevenson
 *
 */
public class User {
    private String fName, lName, id, password;
    private char type;

    public User(String newF, String newL, String newId, String newPw,
            char newType) {
        fName = newF;
        lName = newL;
        id = newId;
        password = newPw;
        type = newType;
    }

    public boolean editType(char newType) {
        if (UserTypes.isValidType(newType)) {
            type = newType;
            return true;
        }
        return false;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public boolean equals(User other) {

        if (other.getId().equals(id)) {
            return true;
        }

        return false;
    }
}
