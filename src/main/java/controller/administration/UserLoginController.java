package controller.administration;

import run.Launcher;
import model.administration.User;
import model.administration.UserDB;
import model.driver.Debug;
import model.driver.Grader;
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

    /**
     * Users enter their user name in here.
     */
	@FXML
	private TextField user_name;

	/**
	 * Users enter their password in here.
	 */
	@FXML
	private PasswordField password;

	/**
	 * Launches the new user dialog.
	 */
	@FXML
	private Hyperlink new_user;

	/**
	 * Launches the main page, provided user login info is correct.
	 */
	@FXML
	private Button button_login;

	/**
	 * Exits the program.
	 */
	@FXML
	private Button button_cancel;

	/**
	 * Becomes visible if the user's login info is incorrect.
	 */
	@FXML
	private Label login_invalid;

	/**
	 * Handles the login button click. If login is successful, launches the MainPage view.
	 */
	@FXML
	public void login() {
		Debug.log("Login", "attempting login...");

		UserDB users = Grader.getUserDB();
		String userName = user_name.getText();
		String pass = password.getText();

		User targetUser = users.login(userName, pass);

		// check db
		if (targetUser != null) {
			Debug.log("Login", "logged in as " + userName + '\n');
			Grader.setUser(targetUser);
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
	@FXML
	public void cancel() {
		((Stage) button_cancel.getScene().getWindow()).close();
	}

	/**
	 * Launches the new_user view.
	 */
	@FXML
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
					.getClassLoader().getResource("view/mainpage/MainPage.fxml")));

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
                    .getClassLoader().getResource("view/administration/new_user.fxml")));

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
