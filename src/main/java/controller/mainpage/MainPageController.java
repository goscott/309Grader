package controller.mainpage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

import controller.Alert;
import controller.GraderPopup;
import controller.graph.GraphController;
import controller.history.HistoryController;
import controller.roster.GradebookController;
import run.Launcher;
import testing.administration.PermissionsTester;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.administration.PermissionKeys;
import model.administration.UserDB;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Exporter;
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
	private static Tab gradebookTab;
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
	private MenuItem exportFile;
	/** the export menu item **/
	@FXML
	private MenuItem exportExcel;
	/** The Quit menu item **/
	@FXML
	private MenuItem user_exit;
	/** The save menu item **/
	@FXML
	private MenuItem save;
	/** The request help menu item **/
	@FXML
	private MenuItem requestHelp;
	/** The roster synch menu item **/
	@FXML
	private MenuItem synch;
	/** The settings menu **/
	@FXML
	private Menu settings;
	/** The server menu **/
	@FXML
	private Menu serverMenu;

	@FXML
	private FlowPane buttonSetUp;

	/**
	 * Initializes the main page
	 */
	public void initialize() {
		// gives it some initial data
		Debug.autoPopulate();

		// loads tab contents
		try {
			// add gradebook
			BorderPane gradebookPage = (BorderPane) GraderPopup
					.getResource("view/roster/gradebook_screen.fxml");
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
			SplitPane graphPage = (SplitPane) GraderPopup
					.getResource("view/graph/graph.fxml");
			graphTab.setContent(graphPage);

			// add historytab -Mason
			// (students do not see)
			if (Grader.getUser().getPermissions()
					.contains(PermissionKeys.VIEW_HISTORY)) {
				StackPane historyPage = (StackPane) GraderPopup
						.getResource("view/history/history_screen.fxml");
				historyTab.setContent(historyPage);
			} else {
				historyTab.setDisable(true);
			}
			// add announcementsTab -Jacob
			BorderPane announcemetsPage = (BorderPane) GraderPopup
					.getResource("view/announcements/announcementTab.fxml");
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
			if (!Grader.getUser().getPermissions()
					.contains(PermissionKeys.SERVER_MENU)) {
				serverMenu.setDisable(true);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

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

		initTabPane();
	}

	/**
	 * Initializes the tab pane.
	 */
	private void initTabPane() {
		graphTab.selectedProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue,
					Object newValue) {
				if (graphTab.isSelected()) {
					GraphController.refresh();
				}
				if(gradebookTab.isSelected()) {
					GradebookController.get().fullRefresh();
				}
			}
		});
		// TODO Make work so no extra grade
		gradebookTab.setOnSelectionChanged(new EventHandler<Event>() {
			public void handle(Event event) {
				GradebookController.get().fullRefresh();
			}
		});
		synch.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Stage stage = GraderPopup.getPopupStage("Roster Sync", "view/mainpage/RosterSync.fxml");
				synch.setDisable(true);
				stage.setOnHidden(new EventHandler<WindowEvent>() {
					public void handle(WindowEvent event) {
						synch.setDisable(false);
					}
				});
				stage.show();
			}
		});
	}

	/**
	 * Disables tabs when no roster is selected and refreshes buttons
	 */
	private void disable() {
		graphTab.setDisable(true);
		// historyTab.setDisable(true);
		// predictionsTab.setDisable(true);
		announcementsTab.setDisable(true);
		gradebookTab.setDisable(true);
		exportExcel.setDisable(true);
		exportFile.setDisable(true);
		buttonController.refreshButtons();
		save.setDisable(true);
		serverMenu.setDisable(true);

		gradebookTab.setText("GradeBook");
		graphTab.setText("Graphs");
		announcementsTab.setText("Announcements");
	}

	/**
	 * Enables class-specific tabs
	 */
	private void enable() {
		exportFile.setDisable(false);
		exportExcel.setDisable(false);
		gradebookTab.setDisable(false);
		graphTab.setDisable(false);
		// historyTab.setDisable(false);
		// predictionsTab.setDisable(false);
		announcementsTab.setDisable(false);
		save.setDisable(false);
		serverMenu.setDisable(false);

		gradebookTab.setText("GradeBook - " + Grader.getRoster().courseName());
		graphTab.setText("Graphs - " + Grader.getRoster().courseName());
		/*predictionsTab.setText("Predictions - "
				+ Grader.getRoster().courseName());*/
		announcementsTab.setText("Announcements - "
				+ Grader.getRoster().courseName());
	}
	
	public static boolean isEnabled() {
		return !gradebookTab.isDisabled();
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
		GraderPopup.getPopupStage("Edit User Permissions",
				"view/administration/permissions_editor.fxml").show();

	}

	/**
	 * Launches the "About" page
	 */
	@FXML
	public void launchAbout() {
		try {
			Stage stage = GraderPopup.getPopupStage("About Grader",
					"view/mainpage/Abt.fxml");
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
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
	 * 
	 * @param event
	 */
	@FXML
	private void saveHandler(ActionEvent event) {
		if(!GradebookController.predictionMode) {
			if (Grader.getRoster() != null) {
				Debug.log("Save", Grader.getRoster().courseName() + " saved");
				Roster.save(Grader.getRoster());
				Alert.show(Grader.getRoster().courseName() + " has been saved");
			}
		} else {
			Alert.showWarningDialog(Grader.getRoster().courseName() + " cannot be saved", "You are in prediction mode");
		}
	}

	/**
	 * Archives the current roster
	 */
	@FXML
	private void pushRoster(ActionEvent event) {
		Action response = Alert
				.showWarningDialog("Warning: Cannot be undone",
						"Are you sure you want to push the current roster to the history?");
		if (response == Dialog.ACTION_YES) {
			Debug.log("Roster status change", "Roster pushed to history");
			Grader.getRoster().archive();
			thisController.tabPane.getSelectionModel().select(
					thisController.classTab);
			disable();
			HistoryController.currentInstance.loadClasses();
		}
	}

	/**
	 * Synchs the current roster
	 */
	@FXML
	private void synchRoster(ActionEvent event) {
		Debug.log("Roster synch", "Roster synched with server");
	}

	/**
	 * Handles the export to excel
	 */
	@FXML
	private void handleExportToExcel(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose Export Location");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"Excel Files (*.xls)", "*.xls");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(new Stage());
		if (file != null) {
			Exporter.exportRosterToExcel(Grader.getRoster(), file);
		}
	}

	/**
	 * Handles the export to file
	 */
	@FXML
	private void handleExportToFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose Export Location");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"Roster Files (*.rost)", "*.rost");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(new Stage());
		if (file != null) {
			Exporter.exportRosterToFile(Grader.getRoster(), file);
		}

	}

	/**
	 * Handles the loading of a roster
	 */
	@FXML
	private void handleLoad(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Import Roster");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Roster Files", "*.rost"));
		File file = fileChooser.showOpenDialog(new Stage());
		if (file != null) {
			Roster roster = Roster.load(file.getAbsolutePath());
			Roster.save(roster);
			buttonController.refreshButtons();
			Alert.show(roster.courseName()
					+ " imported. A copy has been saved to Rosters/. This copy is what will be edited by the program.");
		}
	}
}
