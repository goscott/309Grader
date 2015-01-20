package view.mainpage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import view.roster.AddAssignmentDialogController;
import view.roster.GradebookController;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.administration.User;
import model.administration.UserDB;
import model.administration.UserTypes;
import model.administration.tests.PermissionsTester;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Student;

/**
 * Controller for the main GraderTool view
 * @author Mason Stevenson
 * @author Gavin Scott
 *
 */
public class MainPageController {
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab classTab;
	@FXML
	private Tab gradebookTab;
	@FXML
	private Tab graphsTab;
	@FXML
	private Tab historyTab;
	@FXML
	private Tab predictionsTab;
	@FXML
	private Tab announcmentsTab;
	@FXML
	private Menu classMenu;
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

	public void initialize() {
		// gives it some initial data
		Debug.autoPopulate();
		
		
		// loads tab contents
		try {
			// add gradebook
			TableView<?> gradebookPage = (TableView<?>) FXMLLoader
					.load(getClass().getResource(
							"../roster/gradebook_screen.fxml"));
			gradebookTab.setContent(gradebookPage);
			
			// add historytab -Mason
			HBox historyPage = (HBox) FXMLLoader
                    .load(getClass().getResource(
                            "../history/history_screen.fxml"));
			historyTab.setContent(historyPage);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// FILE MENU

		// disable class menu if no current roster
		classMenu.disableProperty().bind(new BooleanBinding() {
			@Override
			protected boolean computeValue() {
				return Grader.getRoster() == null;
			}
		});
		AnchorPane classPane = (AnchorPane) classTab.getContent();
		FlowPane buttonSetUp = new FlowPane();
		classPane.getChildren().add(buttonSetUp);
		buttonSetUp.setTranslateX(25);
		buttonSetUp.setTranslateY(25);
		buttonSetUp.setVgap(50);
		buttonSetUp.setHgap(50);
		buttonSetUp.getChildren().add(new Button("309"));
        buttonSetUp.getChildren().add(new Button("308"));
		//TODO make buttons mean something and build based on files found.
		EventHandler<ActionEvent> action = handleMenuItems();
		addAssignment.setOnAction(action);
		addStudent.setOnAction(action);

	}

	// launches the permissions editor
	public void permissions() {
		System.out.println("Launching permissions editor");
		UserDB users = new UserDB();

		try {
			Stage stage = new Stage();

			// BorderPane page = (BorderPane)
			// FXMLLoader.load(getClass().getResource("PermissionsEditor.fxml"));
			Scene scene = new Scene((Parent) FXMLLoader.load(getClass()
					.getResource("../administration/permissions_editor.fxml")));

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

	private EventHandler<ActionEvent> handleMenuItems() {
		return new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				MenuItem mItem = (MenuItem) event.getSource();
				switch (mItem.getText()) {
				case "Add Assignment":
					Stage newStage = new Stage();
					AddAssignmentDialogController popup = new AddAssignmentDialogController();
					popup.setParent(mItem);
					popup.start(newStage);
					break;

				case "Add Student":
					String name = JOptionPane
							.showInputDialog("Please input a name");
					Grader.addStudent(new Student(name, ""
							+ (name.length() * 236 - name.charAt(0))));
					break;
				}
			}
		};
	}
}
