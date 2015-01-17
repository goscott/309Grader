package view.administration;

import run.Launcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    
    public void togglePassword() {
        if (checkbox_show.isSelected()) {
            showPassword();
        }
        
        else {
            hidePassword();
        }
    }
    
    private void showPassword() {
        password_masked.setVisible(false);
        password_plain_text.setText(password_masked.getText());
        password_plain_text.setVisible(true);
    }
    
    private void hidePassword() {
        password_plain_text.setVisible(false);
        password_masked.setText(password_masked.getText());
        password_masked.setVisible(true);
    }
    
    public void createUser() {
        userLogin();
        ((Stage) button_create.getScene().getWindow()).close();
    }
    
    public void cancel() {
        userLogin();
        ((Stage) button_cancel.getScene().getWindow()).close();
    }
    
    private void userLogin() {
        Launcher launch = new Launcher();
        launch.start(new Stage());
    }
}
