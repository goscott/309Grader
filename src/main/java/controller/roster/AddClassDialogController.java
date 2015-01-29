package controller.roster;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import model.driver.Debug;
import model.roster.Roster;
import model.server.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * controls the Add class dialog functionality incomplete
 * 
 * @author Michael Lenz
 * 
 */
public class AddClassDialogController {
    /** contains the resource bundle **/
	@FXML
	private ResourceBundle resources;
	/** the location of the main fxml file **/
	@FXML
	private URL location;
	/** the cancel button  **/
	@FXML
	private Button CancelButton;
	/** the viewable list of students **/
	@FXML
	private ListView<String> students;
	/** Text field to enter the class name  **/
	@FXML
	private TextField className;
	/** button to add the class **/
	@FXML
	private Button AddClassButton;
	/**
	 * initializes and displays a new 
	 */
	@FXML
	void initialize() {
		ObservableList<String> items = FXCollections
				.observableArrayList("There will be students here someday");
		students.setItems(Server.getStudentListName());
		students.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// students.setItems(items);
	}
	/**
	 * Handels the event of when the addClass button is pressed
	 * @param event event to be handeled
	 */
	@FXML
	private void AddClass(ActionEvent event) {
		Roster roster = new Roster(className.getText(), "DefaultInstructor", 1, "Winter", new Date(),
				new Date());
		roster.Save();
		Debug.log("roster saved");
		((Stage) AddClassButton.getScene().getWindow()).close();
	}
	/**
	 * cances and closes the dialog
	 * @param event
	 */
	@FXML
	void Cancel(ActionEvent event) {
		((Stage) AddClassButton.getScene().getWindow()).close();
	}

	/**
	 * starts the add class dialog
	 * 
	 * @param stage
	 */
	public void start(Stage stage) {

		try {
			Pane page = (Pane) FXMLLoader.load(getClass().getClassLoader()
					.getResource(("view/roster/addClassDialog.fxml")));
			Scene popup = new Scene(page);
			stage.setTitle("Add Class");
			stage.setScene(popup);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e1) {
			Debug.log("IO ERROR", "Could not load file to start popup");
			e1.printStackTrace();
		}

		stage.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				// parent.setDisable(false);
			}
		});
	}

}