package controller.mainpage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;
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

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

import run.Launcher;
import controller.Alert;
import controller.GraderPopup;
import controller.graph.GraphController;
import controller.history.HistoryController;
import controller.roster.GradebookController;

/**
 * Controller for the main GraderTool view
 * 
 * @author Mason Stevenson
 * @author Gavin Scott
 * @author Frank Poole
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
	/** Classes button setup **/
	@FXML
	private FlowPane buttonSetUp;
	/** Scroll pane for classes tab **/
	@FXML
	private ScrollPane classesScrollPane;
	/** The list for local roster **/
	
	
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
								GradebookController.get().fullRefresh();
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
	    (classesScrollPane).setBorder(new Border(new BorderStroke(null, null, null, new BorderWidths(25.0))));
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
		
		gradebookTab.setOnSelectionChanged(getStyleHandler());
		graphTab.setOnSelectionChanged(getStyleHandler());
		classTab.setOnSelectionChanged(getStyleHandler());
		announcementsTab.setOnSelectionChanged(getStyleHandler());
		historyTab.setOnSelectionChanged(getStyleHandler());
	    classTab.setStyle("-fx-background-color: #74D7E5; -fx-text-base-color: #ffffff;");
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
	
	/** Gets a handler that styles the tabs as they are selected
	 * and deselected
	 */
	private EventHandler<Event> getStyleHandler() {
	    return new EventHandler<Event>() {
            public void handle(Event event) {
                GradebookController.get().fullRefresh();
                if(!((Tab)event.getTarget()).isSelected())
                    ((Tab)event.getTarget()).setStyle("-fx-background-color: #2EA1B1; -fx-text-base-color: #ffffff;");
                else
                    ((Tab)event.getTarget()).setStyle("-fx-background-color: #74D7E5; -fx-text-base-color: #ffffff;");
            }
        };
	}

	/**
	 * Disables tabs when no roster is selected and refreshes buttons
	 */
	private void disable() {
		graphTab.setDisable(true);
		announcementsTab.setDisable(true);
		gradebookTab.setDisable(true);
		exportExcel.setDisable(true);
		exportFile.setDisable(true);
		buttonController.refreshButtons();
		save.setDisable(true);
		serverMenu.setDisable(true);
	}

	/**
	 * Enables class-specific tabs
	 */
	private void enable() {
		exportFile.setDisable(false);
		exportExcel.setDisable(false);
		gradebookTab.setDisable(false);
		graphTab.setDisable(false);
		announcementsTab.setDisable(false);
		save.setDisable(false);
		serverMenu.setDisable(false);
	}
	
	/**
	 * Checks if the tabs are enabled
	 */
	public static boolean isEnabled() {
		return !thisController.gradebookTab.isDisabled();
	}

	/**
	 * Allows for static access to the tab-enabler
	 */
	public static void enableTabs() {
		thisController.enable();
	}

	/**
	 * Launches the permissions editor.
	 */
	@FXML
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
			Logger.getLogger(MainPageController.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	/**
	 * Logs the user out
	 */
	@FXML
	public void logout() {
		UserDB users = Grader.getUserDB();
		users.logout();

		// launch login window
		Launcher launch = new Launcher();
		launch.start(new Stage());

		// close this window
		((Stage) tabPane.getScene().getWindow()).close();
	}

	/**
	 * Exits the grader tool program.
	 */
	@FXML
	public void exit() {
		// close the program
		((Stage) tabPane.getScene().getWindow()).close();
	}

	/**
	 * Saves the current roster
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
			Alert.showWarningMessage(Grader.getRoster().courseName() + " cannot be saved", "You are in prediction mode");
		}
	}

	/**
	 * Archives the current roster
	 */
	@FXML
	private void pushRoster(ActionEvent event) {
		Action response = Alert
				.showWarningQuestion("Warning: Cannot be undone",
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
	 * Syncs the current roster
	 */
	@FXML
	private void synchRoster(ActionEvent event) {
		Debug.log("Roster sync", "Roster synced with server");
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
