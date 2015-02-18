package controller.roster;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import controller.mainpage.ClassButtonsController;
import model.driver.Debug;
import model.roster.Roster;
import model.roster.Student;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
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
	/** The parent of the dialog **/
	private static ClassButtonsController parent;
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
	 * Gets a student from the server by their name
	 */
	private Student getStudentByName(String name) {
		for(Student student : Server.getStudents()) {
			if(student.getName().equals(name)) {
				return student;
			}
		}
		return null;
	}
	
	/**
	 * Handels the event of when the addClass button is pressed
	 * @param event event to be handeled
	 */
	@FXML
	private void AddClass(ActionEvent event) {
		Roster roster = new Roster(className.getText(), "DefaultInstructor", 1, "Winter", Calendar.getInstance(),
				Calendar.getInstance());
		for(String name : students.getSelectionModel().getSelectedItems()) {
			roster.addStudent(getStudentByName(name));
		}
		roster.Save();
		Debug.log("Roster created and saved");
		parent.refreshButtons();
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
	 * @param superClass 
	 */
	public void start(Stage stage, ClassButtonsController superClass) {
	    parent = superClass;
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