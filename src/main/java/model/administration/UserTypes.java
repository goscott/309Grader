package model.administration;

/**
 * Holds permission-types info.
 * @author Mason Stevenson
 *
 */
public class UserTypes {

    /**
     * Identifying character for a Student user.
     */
    public static final char USER_STUDENT = 's';

    /**
     * Identifying character for a Instructor user.
     */
    public static final char USER_INSTRUCTOR = 'i';

    /**
     * Identifying character for a Teacher's aid user.
     */
    public static final char USER_TA = 't';

    /**
     * Identifying character for a Administrator user.
     */
    public static final char USER_ADMIN = 'a';

    /**
     * Identifying character for a user with a custom key configuration.
     * (See PermissionKeys.java, PermissionEditorController.java)
     */
    public static final char USER_CUSTOM = 'c';

    /**
     * Checks a target char to see if it a UserType.
     * @param target A char to check.
     * @return Returns true if the char is a UserType char.
     */
    /*@
          ensures 
          (
              \result == (target == USER_STUDENT 
                || target == USER_INSTRUCTOR
                || target == USER_TA
                || target == USER_ADMIN
                || target == USER_CUSTOM)
          );
     @*/
    public static boolean isValidType(char target) {

        return target == USER_STUDENT || target == USER_INSTRUCTOR
                || target == USER_TA || target == USER_ADMIN
                || target == USER_CUSTOM;
    }

    /**
     * Converts a UserType char into the full name it represents.
     * @param target A char to check.
     * @return Returns the full name of the char, or INVALID USER if the 
     * char is not a UserType char.
     */
    /*@
          ensures
          (
              isValidType(target) ==> (!target.equals("INVALID USER"))
              
              &&
              
              !isValidType(target) ==> (target.equals("INVALID USER"))
          );
     @*/
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
        case USER_CUSTOM:
            return "Custom";
        default:
            return "INVALID USER";
        }
    }
}
