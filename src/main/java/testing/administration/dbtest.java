package testing.administration;

import model.administration.*;

/*
 * 
 * @author Mason Stevenson
 *
 */
public class dbtest {

    public static void main(String[] args) {
        UserDB db = new UserDB();
        db.print();
        //db.addUser(new User("New", "User", "" + System.currentTimeMillis(),
                //"f0rk$@ndKNIVES33", 't'));
        //db.removeUser(db.get("msteve07"));
        //db.editUserType(db.get("msteve07"), UserTypes.USER_TA);
        //db.print();
    }
}
