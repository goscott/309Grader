package controller.administration;

import model.administration.User;
import model.administration.UserDB;
import model.administration.UserTypes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controls the new_user view.
 * @author Mason Stevenson
 *
 */
public class NewUserController {
    @FXML
    private TextField first_name;
    
    @FXML
    private TextField last_name;
    
    @FXML
    private TextField user_name;
    
    @FXML
    private PasswordField password_masked;
    
    @FXML
    private TextField password_plain_text;
    
    @FXML
    private CheckBox checkbox_show;
    
    @FXML 
    private Button button_create;
    
    @FXML 
    private Button button_cancel;
    
    @FXML
    private Label error_message;
    
    @FXML
    private MenuItem item_student;
    
    @FXML
    private MenuItem item_ta;
    
    @FXML
    private MenuItem item_instructor;
    
    @FXML
    private MenuItem item_admin;
    
    @FXML
    private MenuButton button_select_permissions;
    
    private char userType = UserTypes.USER_STUDENT;
    
    /**
     * Switches between masked and plaintext views of the password.
     */
    public void togglePassword() {
        if (checkbox_show.isSelected()) {
            password_masked.setVisible(false);
            password_plain_text.setText(password_masked.getText());
            password_plain_text.setVisible(true);
        }
        
        else {
            password_plain_text.setVisible(false);
            password_masked.setText(password_masked.getText());
            password_masked.setVisible(true);
        }
    }
    
    /**
     * Attempts to add a user to the user database based on the info entered by the user.
     */
    public void createUser() {
        
        UserDB users = new UserDB();
        
        if (first_name.getText().isEmpty()) {
            error_message.setVisible(true);
        }
        
        else if (last_name.getText().isEmpty()) {
            error_message.setVisible(true);
        }
        
        else if ((!checkbox_show.isSelected() && password_masked.getText().isEmpty())
                || (checkbox_show.isSelected() && password_plain_text.getText().isEmpty())) {
            error_message.setVisible(true);
        }
        
        //if user is not already in the db
        else if (users.get(user_name.getText()) == null) {
            
            if (!checkbox_show.isSelected()) {
                users.addUser(new User(first_name.getText(), last_name.getText(), user_name.getText(), password_masked.getText(), userType));
            }
            
            else {
                users.addUser(new User(first_name.getText(), last_name.getText(), user_name.getText(), password_plain_text.getText(), userType));
            }
            
            ((Stage) button_create.getScene().getWindow()).close();
        }
        
        else {
            error_message.setVisible(true);
        }
    } 
    
    /**
     * Closes the window.
     */
    public void cancel() {
        ((Stage) button_cancel.getScene().getWindow()).close();
    }
    
    /**
     * Handles a selected element in the permissions chooser.
     */
    public void studentSelected() {
        button_select_permissions.setText("Student");
        userType = UserTypes.USER_STUDENT;
    }
    
    /**
     * Handles a selected element in the permissions chooser.
     */
    public void taSelected() {
        button_select_permissions.setText("Teacher's Aid");
        userType = UserTypes.USER_TA;
    }
    
    /**
     * Handles a selected element in the permissions chooser.
     */
    public void instructorSelected() {
        button_select_permissions.setText("Instructor");
        userType = UserTypes.USER_INSTRUCTOR;
    }
    
    /**
     * Handles a selected element in the permissions chooser.
     */
    public void adminSelected() {
        button_select_permissions.setText("Admin");
        userType = UserTypes.USER_ADMIN;
    }
}
