package controller.mainpage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.roster.GradebookController;
import run.Launcher;
import testing.administration.PermissionsTester;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
	private static MainPageController thisController;
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab classTab;
	@FXML
	private Tab gradebookTab;
	@FXML
	private Tab graphTab;
	@FXML
	private Tab historyTab;
	@FXML
	private Tab predictionsTab;
	@FXML
	private Tab announcementsTab;
	@FXML
	private MenuItem addAssignment;
	@FXML
	private MenuItem dropAssignment;
	@FXML
	private MenuItem importAssignment;
	@FXML
	private MenuItem addStudent;
	@FXML
	private MenuItem dropStudent;
	@FXML
	private MenuItem rosterSync;
	@FXML
	private MenuItem logout;
	@FXML
	private MenuItem user_exit;

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
								GradebookController gbook = GradebookController.getController();
								gbook.fullRefresh();
							}
						}
					});

			// Add graphs
			SplitPane graphPage = (SplitPane) FXMLLoader.load(getClass()
					.getClassLoader().getResource("view/graph/graph.fxml"));
			graphTab.setContent(graphPage);

			// add historytab -Mason
			StackPane historyPage = (StackPane) FXMLLoader.load(getClass()
					.getClassLoader().getResource(
							"view/history/history_screen.fxml"));
			historyTab.setContent(historyPage);

			// add announcementsTab -Jacob
			BorderPane announcemetsPage = (BorderPane) FXMLLoader
					.load(getClass().getClassLoader().getResource(
							"view/announcements/announcementTab.fxml"));
			announcementsTab.setContent(announcemetsPage);

			// add predictions
			HBox predictionsPage = (HBox) FXMLLoader.load(getClass()
					.getClassLoader().getResource(
							"view/predictions/predictions_view.fxml"));
			predictionsTab.setContent(predictionsPage);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		AnchorPane classPane = (AnchorPane) classTab.getContent();
		FlowPane buttonSetUp = new FlowPane();
		classPane.getChildren().add(buttonSetUp);
		ClassButtonsController con = new ClassButtonsController(buttonSetUp);
		thisController = this;

		graphTab.setDisable(true);
		historyTab.setDisable(true);
		predictionsTab.setDisable(true);
		announcementsTab.setDisable(true);
		gradebookTab.setDisable(true);

	}

	private void enable() {
		gradebookTab.setDisable(false);
		graphTab.setDisable(false);
		historyTab.setDisable(false);
		predictionsTab.setDisable(false);
		announcementsTab.setDisable(false);
	}

	public static void enableTabs() {
		thisController.enable();
	}

	// launches the permissions editor
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
	
	@FXML
	private void saveHandler(ActionEvent event) {
		Debug.log("Save", Grader.getRoster().courseName() + " saved");
		Roster.save(Grader.getRoster());
	}
}
