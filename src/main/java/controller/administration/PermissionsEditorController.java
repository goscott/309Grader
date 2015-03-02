package controller.administration;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.administration.PermissionKeys;
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
    private ListView<String> user_list;
    
    /**
     * A list of user keys that are available.
     */
    @FXML
    private ListView<String> keys_available;
    
    /**
     * A list of user keys that have been used.
     */
    @FXML
    private ListView<String> keys_used;
    
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
     * A field to search for users.
     */
    @FXML
    private TextField search_field;
    
    /**
     * A list of all currently registered users.
     */
    private UserDB users = Grader.getUserDB();
    
    /**
     * Keeps track of a user type.
     */
    private char userType;
    
    /**
     * Runs on startup.
     */
    public void initialize() {
        
        fullList();
        user_list.setStyle("-fx-font: 10pt \"Lucida Console\";");
        search_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                search();
            }
        });
    }
    
    /**
     * Gets the full user list.
     */
    private void fullList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        
        for (User target : users.getUsers()) {
            list.add(String.format(" %-36s%36s", target.getId(), UserTypes.fullName(target.getType())));
        }
        
        user_list.setItems(list);
    }
    
    /**
     * Switches the view to edit-user mode.
     */
    public void editPermissions() {
        
        user_select.setVisible(false);
        user_edit.setVisible(true);
        
        user_id.setText(((String)user_list.getSelectionModel().getSelectedItem()).split(" ")[1]);
        user_first_last.setText(users.get(user_id.getText()).getfName() + " " + users.get(user_id.getText()).getlName());
        select_permissions.setText(UserTypes.fullName(users.get(user_id.getText()).getType()));
        
        refreshKeysUsed();
        refreshKeysAvailable();
    }
    
    /**
     * Commits the changes.
     */
    public void permissionsChanged() {
        ArrayList<PermissionKeys> keyList = new ArrayList<PermissionKeys>();
        
        //if the currently selected user's type does not match the currently selected type
        if (users.get(user_id.getText()).getType() != userType || userType == UserTypes.USER_CUSTOM) {
            
            if (userType == UserTypes.USER_CUSTOM) {
                
                //set the custom key configuration
                for(String key : keys_used.getItems()) {
                    keyList.add(PermissionKeys.valueOf(key));
                }
                
                users.get(user_id.getText()).setPermissions(keyList);
            }
            
          //Edit that user's permissions based on selected permission type
            users.editUserType(users.get(user_id.getText()), userType);
            
            ObservableList<String> list = FXCollections.observableArrayList();

            for (User target : users.getUsers()) {
                list.add(String.format(" %-36s%36s", target.getId(),
                        UserTypes.fullName(target.getType())));
            }
 
            user_list.getItems().clear();
            user_list.setItems(list);
        } 
        
        user_select.setVisible(true);
        user_edit.setVisible(false);
    }
    
    /**
     * 
     */
    public void search() {
        
        if (search_field.getText().equals("")) {
            fullList();
        }
        
        else {
            ObservableList<String> list = FXCollections.observableArrayList();
            
            for (User target : users.getUsers()) {
                if (target.getId().toLowerCase().startsWith(search_field.getText().toLowerCase()) || 
                        target.getfName().toLowerCase().startsWith(search_field.getText().toLowerCase()) || 
                        target.getlName().toLowerCase().startsWith(search_field.getText().toLowerCase()) ||
                        UserTypes.fullName(target.getType()).toLowerCase().equals(search_field.getText().toLowerCase())) {
                    list.add(String.format(" %-36s%36s", target.getId(), UserTypes.fullName(target.getType())));
                }
            }
            
            user_list.setItems(list);
        }
    }
    
    /**
     * Refreshes the list of used keys, pulling data from the user.
     */
    private void refreshKeysUsed() {
        ObservableList<String> used_list = FXCollections.observableArrayList();
        
        for (PermissionKeys key : users.get(user_id.getText()).getPermissions()) {
            used_list.add(key.toString());
        }
        
        keys_used.getItems().clear();
        keys_used.setItems(used_list);
    }
    
    /**
     * Refreshes the list of used keys, pulling data from the defaults.
     */
    private void refreshKeysUsed(char userType) {
        ObservableList<String> used_list = FXCollections.observableArrayList();
        
        for (PermissionKeys key : PermissionKeys.getKeys(userType)) {
            used_list.add(key.toString());
        }
        
        keys_used.getItems().clear();
        keys_used.setItems(used_list);
    }
    
    /**
     * Refreshes the list of used keys
     */
    private void refreshKeysAvailable() {
        ObservableList<String> available_list = FXCollections.observableArrayList();
        
        for (PermissionKeys key : PermissionKeys.values()) {
            if (!keys_used.getItems().contains(key.toString())) {
                available_list.add(key.toString());
            }
        }
        
        keys_available.getItems().clear();
        keys_available.setItems(available_list);
    }
    
    /**
     * Adds a key from available list to used list.
     */
    public void addKey() {
        String key = keys_available.getSelectionModel().getSelectedItem();
        int indexOfKey = keys_available.getSelectionModel().getSelectedIndex();
        
        if (indexOfKey >= 0) {
            keys_available.getItems().remove(indexOfKey);
            keys_used.getItems().add(key);
            userType = UserTypes.USER_CUSTOM;
            select_permissions.setText("Custom");
        }
    }
    
    /**
     * Adds a key from used list to available list.
     */
    public void removeKey() {
        String key = keys_used.getSelectionModel().getSelectedItem();
        int indexOfKey = keys_used.getSelectionModel().getSelectedIndex();
        
        if (indexOfKey >= 0) {
            keys_used.getItems().remove(indexOfKey);
            keys_available.getItems().add(key);
            userType = UserTypes.USER_CUSTOM; 
            select_permissions.setText("Custom");
        }
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
        refreshKeysUsed(userType);
        refreshKeysAvailable();
    }
    
    /**
     * Handles a selected element in the permissions chooser.
     */
    public void taSelected() {
        select_permissions.setText("Teacher's Aid");
        userType = UserTypes.USER_TA;
        refreshKeysUsed(userType);
        refreshKeysAvailable();
    }
    
    /**
     * Handles a selected element in the permissions chooser.
     */
    public void instructorSelected() {
        select_permissions.setText("Instructor");
        userType = UserTypes.USER_INSTRUCTOR;
        refreshKeysUsed(userType);
        refreshKeysAvailable();
    }
    
    /**
     * Handles a selected element in the permissions chooser.
     */
    public void adminSelected() {
        select_permissions.setText("Admin");
        userType = UserTypes.USER_ADMIN;
        refreshKeysUsed(userType);
        refreshKeysAvailable();
    }
}
