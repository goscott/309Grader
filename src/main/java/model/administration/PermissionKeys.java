package model.administration;

import java.util.ArrayList;
import java.util.Arrays;

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
