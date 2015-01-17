package view.administration;

import model.administration.User;
import model.administration.UserDB;
import javafx.application.Application;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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

	public void login() {
		System.out.println("logging in");

		UserDB users = new UserDB();
		String userName = user_name.getText();
		String pass = password.getText();

		User targetUser = users.login(userName, pass);

		System.out.println("user: " + userName + " pass: " + pass);

		// check db
		if (targetUser != null) {
			System.out.println("login successful!");

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

	public void newUser() {
		launchNewUser();
		((Stage) new_user.getScene().getWindow()).close();
	}

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
	
	private void launchNewUser() {
	    try {
            Stage stage = new Stage();
            Scene scene = new Scene((Parent) FXMLLoader.load(getClass()
                    .getResource("new_user.fxml")));

            stage.setScene(scene);
            stage.setTitle("Create New User");

            stage.show();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
	}

}
