package administration;

/**
 * 
 * @author Mason Stevenson
 *
 */
public class UserTypes {

    public static final char USER_STUDENT = 's';
    public static final char USER_INSTRUCTOR = 'i';
    public static final char USER_TA = 't';

    public static boolean isValidType(char target) {

        return target == USER_STUDENT || target == USER_INSTRUCTOR
                || target == USER_TA;
    }
}
