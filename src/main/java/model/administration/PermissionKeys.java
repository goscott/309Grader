package model.administration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contains a list of specific components of the GraderTool that have restricted access for some users.
 * 
 * If you want to restrict access to one of your views: 
 *      1) Add a new PermissionKey value.
 *      2) Look at the PermissionKeys.getKeys() method and add your key to whichever Users you want to have access to it.
 *      3) Now you can check if the current user has access to your key by calling: 
 *      
 *          if(Grader.getUser().getPermissions().contains(PermissionKeys.MY_KEY)) {
 *              //show item
 *          }
 * 
 * @author Mason S
 *
 */
public enum PermissionKeys {
    
    /**
     * Add unique permission keys here
     */
    
    //MenuBar Permissions
    SAVE,
    EXPORT,
    LOAD,
    SERVER_MENU,
    EDIT_PERMISSIONS,
    PUSH_TO_HISTORY,
    
    //Classes Tab Permissions
    ADD_CLASS,
    
    //GradeBook Permissions
    VIEW_STUDENTS,
    COMMIT_EDITS,
    
    //Graphs Permissions
    
    
    //History Permissions
    VIEW_HISTORY,
    
    //Announcements Permissions
    
    //Predictions Permissions
    EDIT_GRADEBOOK
    
    //END
    ;
    
    /**
     * Given a user type character (See UserTypes.java) this returns a default configuration of 
     * permission keys in an ArrayList.
     */
    /*@
          requires
              UserTypes.isValidType(userType);
     @*/
    public static ArrayList<PermissionKeys> getKeys(char userType) {
        ArrayList<PermissionKeys> result = new ArrayList<PermissionKeys>();
        PermissionKeys[] list = {};
        
        switch(userType) {
        
            case(UserTypes.USER_STUDENT):
                list = new PermissionKeys[]{EXPORT, LOAD, EDIT_GRADEBOOK};
                break;
            
            case(UserTypes.USER_TA):
                list = new PermissionKeys[]{SAVE, EXPORT, LOAD, EDIT_GRADEBOOK, VIEW_STUDENTS};
                break;
            
            case(UserTypes.USER_INSTRUCTOR):
                list = PermissionKeys.values();
                break;
            
            case(UserTypes.USER_ADMIN):
                list = PermissionKeys.values();
                break;
            
            default:
                break;
        }
        
        result.addAll((Arrays.asList(list)));
        return result;
    }
}
