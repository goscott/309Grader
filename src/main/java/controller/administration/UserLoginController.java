package controller.administration;

import com.sun.javafx.scene.control.skin.VirtualFlow;

import controller.GraderPopup;
import controller.roster.GradebookController;
import resources.ResourceLoader;
import run.Launcher;
import model.administration.User;
import model.administration.UserDB;
import model.driver.Debug;
import model.driver.Grader;
import model.server.Server;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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

	/** The stage for the loading window **/
	private static Stage loadStage;
	
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
			loadMainPage();

			// close out the login window
			//((Stage) button_login.getScene().getWindow()).close();
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

	private void loadMainPage() {
		loadStage = new Stage(StageStyle.UNDECORATED);
		((Stage) button_cancel.getScene().getWindow()).close();
		loadStage.show();
		
		StackPane sp = new StackPane();
		ImageView imgView = new ImageView(ResourceLoader.LOAD_SCREEN);
		sp.getChildren().add(imgView);
		
		Scene scene = new Scene(sp);
		loadStage.setScene(scene);
		loadStage.setAlwaysOnTop(true);
		loadStage.centerOnScreen();
		
		launchMainPage();
	}
	
	/**
	 * Closes the loading screen
	 */
	public static void closeLoadScreen() {
		if(loadStage != null) {
			loadStage.hide();
		}
	}
	
	/**
	 * Launches the main page.
	 */
	private void launchMainPage() {
		try {
			Stage stage = GraderPopup.getPopupStage("view/mainpage/MainPage.fxml");
			stage.setResizable(true);
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent event) {
					Server.backup();
				}
			});
			stage.show();
			
			//Links the horizontal scrollbars for the two tables in the gradebook
			//This only works if you put the code here, where the mainpage is initialized
			ScrollBar table1Scroller = findHorizontalScrollBar(GradebookController.getController().getMainTable());  
	        ScrollBar table2Scroller = findHorizontalScrollBar(GradebookController.getController().getStatsTable());  
	        
	        table1Scroller.valueProperty().bindBidirectional(table2Scroller.valueProperty());
			closeLoadScreen();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Locates the horizontal scroll bar for a table
	 */
	@SuppressWarnings({ "rawtypes", "restriction" })
    private ScrollBar findHorizontalScrollBar(TableView<?> table) {  
        //return (ScrollBar) table.lookup(".scroll-bar:vertical");  
        VirtualFlow vf = (VirtualFlow)table.getChildrenUnmodifiable().get(1);
        ScrollBar scrollBar1 = null;
        for (final Node subNode: vf.getChildrenUnmodifiable()) {
            if (subNode instanceof ScrollBar && 
                    ((ScrollBar)subNode).getOrientation() == Orientation.HORIZONTAL) {
                scrollBar1 = (ScrollBar)subNode;
            }
         }
        
        return scrollBar1;
    } 
	
	/**
	 * Launches the new user dialog.
	 */
	private void launchNewUser() {
	    try {
            Stage stage = GraderPopup.getPopupStage("Create New User", "view/administration/new_user.fxml");
            stage.setOnHiding(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent event) {
                    Launcher launch = new Launcher();
                    launch.start(new Stage());
                }
            });
            stage.show();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
	}

}