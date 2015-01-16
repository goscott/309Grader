package model.administration;

/**
 * 
 * @author Mason Stevenson
 *
 */
public class UserTypes {

    public static final char USER_STUDENT = 's';
    public static final char USER_INSTRUCTOR = 'i';
    public static final char USER_TA = 't';
    public static final char USER_ADMIN = 'a';

    public static boolean isValidType(char target) {

        return 
                target == USER_STUDENT 
                || target == USER_INSTRUCTOR
                || target == USER_TA
                || target == USER_ADMIN;
    }
    
    public static String fullName(char target) {
        switch (target) {
            case USER_STUDENT:
                return "Student";
            case USER_INSTRUCTOR:
                return "Instructor";
            case USER_TA:
                return "Teachers Aid";
            case USER_ADMIN:
                return "Admin";
            default:
                return "INVALID USER";
        }
    }
}
