package controller.mainpage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.history.HistoryController;
import controller.roster.GradebookController;
import run.Launcher;
import testing.administration.PermissionsTester;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.administration.PermissionKeys;
import model.administration.User;
import model.administration.UserDB;
import model.administration.UserTypes;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Roster;

/**
 * Controller for the main GraderTool view
 * 
 * @author Mason Stevenson, Gavin Scott, Frank Poole
 *
 */
public class MainPageController {
	/** Singleton controller **/
	private static MainPageController thisController;
	/** The controller for the buttons **/
	private static ClassButtonsController buttonController;
	/** The main tab pane **/
	@FXML
	private TabPane tabPane;
	/** The class tab **/
	@FXML
	private Tab classTab;
	/** The gradebook tab **/
	@FXML
	private Tab gradebookTab;
	/** The graph tab **/
	@FXML
	private Tab graphTab;
	/** The history tab **/
	@FXML
	private Tab historyTab;
	/** The prediction tab **/
	/*@FXML
	private Tab predictionsTab;*/
	/** The announcements tab **/
	@FXML
	private Tab announcementsTab;
	/** The logout menu item **/
	@FXML
	private MenuItem logout;
	/** the export menu item **/
	@FXML
	private MenuItem export;
	/** The Quit menu item **/
	@FXML
	private MenuItem user_exit;
	/** The save menu item **/
	@FXML
	private MenuItem save;
	/** The request help menu item **/
	@FXML
	private MenuItem requestHelp;
	/** The settings menu **/
	@FXML
	private Menu settings;
	/** The server menu **/
	@FXML
	private Menu serverMenu;

	/**
	 * Initializes the main page
	 */
	public void initialize() {
		// gives it some initial data
		Debug.autoPopulate();

		// loads tab contents
		try {
			// add gradebook
			TableView<?> gradebookPage = (TableView<?>) FXMLLoader
					.load(getClass().getClassLoader().getResource(
							"view/roster/gradebook_screen.fxml"));
			gradebookTab.setContent(gradebookPage);
			gradebookTab.selectedProperty().addListener(
					new ChangeListener<Boolean>() {
						@Override
						public void changed(
								ObservableValue<? extends Boolean> arg0,
								Boolean oldPropertyValue,
								Boolean newPropertyValue) {
							if (newPropertyValue) {
								GradebookController gbook = GradebookController
										.getController();
								gbook.fullRefresh();
							}
						}
					});

			// Add graphs
			SplitPane graphPage = (SplitPane) FXMLLoader.load(getClass()
					.getClassLoader().getResource("view/graph/graph.fxml"));
			graphTab.setContent(graphPage);

			// add historytab -Mason
			// (students do not see)
			if (Grader.getUser().getPermissions().contains(PermissionKeys.VIEW_HISTORY)) {
				StackPane historyPage = (StackPane) FXMLLoader.load(getClass()
						.getClassLoader().getResource(
								"view/history/history_screen.fxml"));
				historyTab.setContent(historyPage);
			} else {
				historyTab.setDisable(true);
			}
			// add announcementsTab -Jacob
			BorderPane announcemetsPage = (BorderPane) FXMLLoader
					.load(getClass().getClassLoader().getResource(
							"view/announcements/announcementTab.fxml"));
			announcementsTab.setContent(announcemetsPage);

			// add predictions
			// (students do not see)
			/*
			if (Grader.getUser().getType() != UserTypes.USER_STUDENT) {
    			HBox predictionsPage = (HBox) FXMLLoader.load(getClass()
    					.getClassLoader().getResource(
    							"view/predictions/predictions_view.fxml"));
    			predictionsTab.setContent(predictionsPage);
			} else {
			    predictionsTab.setDisable(true);
			    serverMenu.setDisable(true);
			}
			*/
			// turn off server menu for students and TAs
			if (!Grader.getUser().getPermissions().contains(PermissionKeys.SERVER_MENU)) {
				serverMenu.setDisable(true);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		AnchorPane classPane = (AnchorPane) classTab.getContent();
		FlowPane buttonSetUp = new FlowPane();
		classPane.getChildren().add(buttonSetUp);
		ClassButtonsController con = new ClassButtonsController(buttonSetUp);
		buttonController = con;
		thisController = this;

		disable();

		// students can't save changes
		if (!Grader.getUser().getPermissions().contains(PermissionKeys.SAVE)) {
			save.setVisible(false);
			settings.setVisible(false);
		} else {
			requestHelp.setVisible(false);
		}
	}
	
	/**
	 * Disables tabs when no roster is selected and refreshes buttons
	 */
	private void disable() {
		graphTab.setDisable(true);
		// historyTab.setDisable(true);
		//predictionsTab.setDisable(true);
		announcementsTab.setDisable(true);
		gradebookTab.setDisable(true);
		export.setDisable(true);
		buttonController.refreshButtons();
		
		gradebookTab.setText("GradeBook");
		graphTab.setText("Graphs");
		announcementsTab.setText("Announcements");
	}
	
	/**
	 * Enables class-specific tabs
	 */
	private void enable() {
		export.setDisable(false);
		gradebookTab.setDisable(false);
		graphTab.setDisable(false);
		// historyTab.setDisable(false);
		//predictionsTab.setDisable(false);
		announcementsTab.setDisable(false);

		gradebookTab.setText("GradeBook - " + Grader.getRoster().courseName());
		graphTab.setText("Graphs - " + Grader.getRoster().courseName());
		/*predictionsTab.setText("Predictions - "
				+ Grader.getRoster().courseName());*/
		announcementsTab.setText("Announcements - "
				+ Grader.getRoster().courseName());
	}

	/**
	 * Allows for static access to the tab-enabler
	 */
	public static void enableTabs() {
		thisController.enable();
	}

	@FXML
	/**
	 * Launches the permissions editor.
	 */
	public void permissions() {
		Debug.log("Launching permissions editor");
		UserDB users = Grader.getUserDB();

		try {
			Stage stage = new Stage();

			// BorderPane page = (BorderPane)
			// FXMLLoader.load(getClass().getResource("PermissionsEditor.fxml"));
			Scene scene = new Scene((Parent) FXMLLoader.load(getClass()
					.getClassLoader().getResource(
							"view/administration/permissions_editor.fxml")));

			@SuppressWarnings("unchecked")
			ListView<String> view = (ListView<String>) scene
					.lookup("#user_list");
			ObservableList<String> list = FXCollections.observableArrayList();

			for (User target : users.getUsers()) {
				list.add(String.format("%-40s%51s", target.getId(),
						UserTypes.fullName(target.getType())));
			}

			view.setItems(list);

			stage.setScene(scene);
			stage.setTitle("Edit User Permissions");
			stage.setResizable(false);

			stage.show();

		} catch (Exception ex) {
			Logger.getLogger(PermissionsTester.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	/**
	 * Launches the "About" page
	 */
	@FXML
	public void launchAbout() {
		try {
			Stage stage = new Stage();

			Scene scene = new Scene((Parent) FXMLLoader.load(getClass()
					.getClassLoader().getResource("view/mainpage/Abt.fxml")));

			stage.setScene(scene);
			stage.setTitle("About Team");
			stage.setResizable(false);
			stage.show();

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					// TODO Auto-generated method stub
					AbtController.muteStatic();
				}
			});

		} catch (Exception ex) {
			Logger.getLogger(PermissionsTester.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	@FXML
	/**
	 * Logs the user out
	 */
	public void logout() {
		UserDB users = Grader.getUserDB();
		users.logout();

		// launch login window
		Launcher launch = new Launcher();
		launch.start(new Stage());

		// close this window
		((Stage) tabPane.getScene().getWindow()).close();
	}

	@FXML
	/**
	 * Exits the grader tool program.
	 */
	public void exit() {
		// close the program
		((Stage) tabPane.getScene().getWindow()).close();
	}

	/**
	 * Saves the current roster
	 * @param event
	 */
	@FXML
	private void saveHandler(ActionEvent event) {
		Debug.log("Save", Grader.getRoster().courseName() + " saved");
		Roster.save(Grader.getRoster());
	}
	
	/**
	 * Archives the current roster
	 */
	@FXML
	private void pushRoster(ActionEvent event) {
		Debug.log("Roster status change", "Roster pushed to history");
		Grader.getRoster().archive();
		thisController.tabPane.getSelectionModel().select(thisController.classTab);
		disable();
		
		/*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/history/history_screen.fxml"));
        try 
        {
            //you have to call this or it doesn't work for some reason
            Pane pane = (Pane) loader.load(); 
        } 
        
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HistoryController controller = loader.<HistoryController>getController();
        //controller.loadClasses();
        controller.call();
        */
		 HistoryController controller = Grader.getHistoryController();
		 controller.loadClasses();
	}
	
	/**
	 * Synchs the current roster
	 */
	@FXML
	private void synchRoster(ActionEvent event) {
		Debug.log("Roster synch", "Roster synched with server");
	}
	
	@FXML
	private void handleExport(ActionEvent event) {
		Grader.getRoster().export();
	}
}
