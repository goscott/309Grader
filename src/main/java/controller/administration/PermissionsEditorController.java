package controller.administration;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import model.administration.User;
import model.administration.UserDB;
import model.administration.UserTypes;
import model.driver.Grader;

/**
 * Controls the permissions_editor view.
 * @author Mason Stevenson
 *
 */
public class PermissionsEditorController {
    
    /**
     * The main user selection pane.
     */
    @FXML
    private VBox user_select;
    
    /**
     * The main user editing pane.
     */
    @FXML
    private VBox user_edit;
    
    /**
     * The list of users in user_select.
     */
    @FXML
    private ListView user_list;
    
    /**
     * The user id of a user.
     */
    @FXML
    private Label user_id;
    
    /**
     * The first and last name of a user.
     */
    @FXML
    private Label user_first_last;
    
    /**
     * A dropdown menu allowing the user to select a permissions-level for a user.
     */
    @FXML
    private MenuButton select_permissions;
    
    /**
     * An item in the the dropdown menu, select_permissions.
     */
    @FXML
    private MenuItem item_student;
    
    /**
     * An item in the the dropdown menu, select_permissions.
     */
    @FXML
    private MenuItem item_ta;
    
    /**
     * An item in the the dropdown menu, select_permissions.
     */
    @FXML
    private MenuItem item_instructor;
    
    /**
     * An item in the the dropdown menu, select_permissions.
     */
    @FXML
    private MenuItem item_admin;
    
    /**
     * A list of all currently registered users.
     */
    private UserDB users = Grader.getUserDB();
    
    /**
     * Keeps track of a user type.
     */
    private char userType;
    
    /**
     * Switches the view to edit-user mode.
     */
    public void editPermissions() {
        
        user_select.setVisible(false);
        user_edit.setVisible(true);
        
        user_id.setText(((String)user_list.getSelectionModel().getSelectedItem()).split(" ")[0]);
        user_first_last.setText(users.get(user_id.getText()).getfName() + " " + users.get(user_id.getText()).getlName());
        select_permissions.setText(UserTypes.fullName(users.get(user_id.getText()).getType()));
    }
    
    /**
     * Commits the changes.
     */
    public void permissionsChanged() {
        //get currently selected userID from gui
        //obtain User objet from UserDB based on userID
        //Edit that user's permissions based on selected permission type
        if (users.get(user_id.getText()).getType() != userType) {
            users.editUserType(users.get(user_id.getText()), userType);
            
            ObservableList<String> list = FXCollections.observableArrayList();

            for (User target : users.getUsers()) {
                list.add(String.format("%-40s%51s", target.getId(),
                        UserTypes.fullName(target.getType())));
            }
 
            user_list.getItems().clear();
            user_list.setItems(list);
        } 
        
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
    
    /**
     * Handles a selected element in the permissions chooser.
     */
    public void studentSelected() {
        select_permissions.setText("Student");
        userType = UserTypes.USER_STUDENT;
    }
    
    /**
     * Handles a selected element in the permissions chooser.
     */
    public void taSelected() {
        select_permissions.setText("Teacher's Aid");
        userType = UserTypes.USER_TA;
    }
    
    /**
     * Handles a selected element in the permissions chooser.
     */
    public void instructorSelected() {
        select_permissions.setText("Instructor");
        userType = UserTypes.USER_INSTRUCTOR;
    }
    
    /**
     * Handles a selected element in the permissions chooser.
     */
    public void adminSelected() {
        select_permissions.setText("Admin");
        userType = UserTypes.USER_ADMIN;
    }
}
