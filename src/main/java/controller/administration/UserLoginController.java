package controller.administration;

import run.Launcher;
import model.administration.User;
import model.administration.UserDB;
import model.driver.Debug;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Controller for the user_login view.
 * @author Mason Stevenson
 *
 */
public class UserLoginController {

	@FXML
	private TextField user_name;

	@FXML
	private PasswordField password;

	@FXML
	private Hyperlink new_user;

	@FXML
	private Button button_login;

	@FXML
	private Button button_cancel;

	@FXML
	private Label login_invalid;

	/**
	 * Handles the login button click. If login is successful, launches the MainPage view.
	 */
	public void login() {
		Debug.log("Login", "attempting login...");

		UserDB users = new UserDB();
		String userName = user_name.getText();
		String pass = password.getText();

		User targetUser = users.login(userName, pass);

		// check db
		if (targetUser != null) {
			Debug.log("Login", "logged in as " + userName + '\n');
			
			// LAUNCH GRADER TOOL HERE
			launchMainPage();

			// close out the login window
			((Stage) button_login.getScene().getWindow()).close();
		}

		else {
			login_invalid.setVisible(true);
		}
	}

	/**
	 * Closes the window
	 */
	public void cancel() {
		((Stage) button_cancel.getScene().getWindow()).close();
	}

	/**
	 * Launches the new_user view.
	 */
	public void newUser() {
	    launchNewUser();
        ((Stage) new_user.getScene().getWindow()).close();
	}

	/**
	 * Launches the main page.
	 */
	private void launchMainPage() {

		try {
			Stage stage = new Stage();
			Scene scene = new Scene((BorderPane) FXMLLoader.load(getClass()
					.getResource("../mainpage/MainPage.fxml")));

			stage.setScene(scene);
			stage.setTitle("GraderTool");

			stage.show();
		}

		catch (Exception e) {

		}
	}
	
	/**
	 * Launches the new user dialog.
	 */
	private void launchNewUser() {
	    try {
            Stage stage = new Stage();
            Scene scene = new Scene((Parent) FXMLLoader.load(getClass()
                    .getResource("new_user.fxml")));

            stage.setScene(scene);
            stage.setTitle("Create New User");

            stage.show();
            
            stage.setResizable(false);
            
            stage.setOnHiding(new EventHandler<WindowEvent>() {

                public void handle(WindowEvent event) {
                    Launcher launch = new Launcher();
                    launch.start(new Stage());
                }
            });
        }

        catch (Exception e) {
            e.printStackTrace();
        }
	}

}
