package view.administration;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import model.administration.UserDB;

/**
 * Controls the permissions_editor view.
 * @author Mason Stevenson
 *
 */
public class PermissionsEditorController {
    
    @FXML
    private VBox user_select;
    
    @FXML
    private VBox user_edit;
    
    @FXML
    private ListView user_list;
    
    @FXML
    private Label user_id;
    
    UserDB users = new UserDB();
    
    /**
     * Switches the view to edit-user mode.
     */
    public void editPermissions() {
        
        user_select.setVisible(false);
        user_edit.setVisible(true);
        
        user_id.setText(((String)user_list.getSelectionModel().getSelectedItem()).split(" ")[0]);
    }
    
    /**
     * Commits the changes.
     */
    public void permissionsChanged() {
        //get currently selected userID from gui
        //obtain User objet from UserDB based on userID
        //Edit that user's permissions based on selected permission type
        user_select.setVisible(true);
        user_edit.setVisible(false);
    }
    
    /**
     * Switches the view back to user-list mode.
     */
    public void cancel() {
        user_select.setVisible(true);
        user_edit.setVisible(false);
    }
}
