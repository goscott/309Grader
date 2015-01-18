package model.administration;

/**
 * Holds permission-types info.
 * @author Mason Stevenson
 *
 */
public class UserTypes {

    public static final char USER_STUDENT = 's';
    public static final char USER_INSTRUCTOR = 'i';
    public static final char USER_TA = 't';
    public static final char USER_ADMIN = 'a';

    /**
     * Checks a target char to see if it a UserType.
     * @param target A char to check.
     * @return Returns true if the char is a UserType char.
     */
    public static boolean isValidType(char target) {

        return 
                target == USER_STUDENT 
                || target == USER_INSTRUCTOR
                || target == USER_TA
                || target == USER_ADMIN;
    }
    
    /**
     * Converts a UserType char into the full name it represents.
     * @param target A char to check.
     * @return Returns the full name of the char, or INVALID USER if the char is not a UserType char.
     */
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
