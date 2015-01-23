package controller.roster;

import java.io.IOException;

import model.driver.Debug;
import model.driver.Grader;
import model.roster.GradedItem;
import model.roster.Student;
import model.server.Server;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * controls the Add Student dialog functionality incomplete
 * @author Shelli Crispen
 * 
 */
public class AddStudentDialogController {

	@FXML
	private ComboBox<String> SelectStudentMenu;
	
	@FXML
	private Button AddStudentButton;
	
	@FXML
	private Button CancelStudentButton;
	
	@SuppressWarnings("rawtypes")
	private static GradebookController gbook;
	private static MenuItem parent;
	private final int maxChars = 25;
	private final String noParent = "<None>";
	
	/**
	 * Sets the parent of the window, so it can grab information from the
	 * gradebook table
	 * 
	 * @param newParent the parent
	 */
	public void setParent(MenuItem newParent, GradebookController gbook) {
		this.gbook = gbook;
		parent = newParent;
		parent.setDisable(true);
	}
	
	/**
	 * Initializes some of the properties. Makes the add button the default.
	 * Populates the parent list.
	 */
	public void initialize() {
		// disables the add button
		AddStudentButton.setDisable(true);
		// populates parent list
		resetDropdown();
		SelectStudentMenu.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean oldPropertyValue, Boolean newPropertyValue) {
				if (newPropertyValue) {
					resetDropdown();
				}
			}
		});
	}
	
	/**
	 * Creates the popup on the given stage
	 * 
	 * @param stage the stage
	 */
	public void start(Stage stage) {
		try {
			BorderPane page = (BorderPane) FXMLLoader.load(getClass()
					.getResource("../../view/roster/AddStudent.fxml"));
			Scene popup = new Scene(page);
			stage.setTitle("Add Student");
			stage.setScene(popup);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e1) {
			Debug.log("IO ERROR", "Could not load file to start popup");
			e1.printStackTrace();
		}

		stage.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				parent.setDisable(false);
			}
		});
	}
	
	@FXML
	/**
	 * Handles the Add button. Closes the window.
	 * @param event the button's event
	 */
	private void handleAddButton(ActionEvent event){
		
		//Closes the popup
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.hide();
	}
	
	@FXML
	/**
	 * Handles the cancel button. Closes the window.
	 * @param event the button's event
	 */
	private void handleCancelButton(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.hide();
	}
	
	/**
	 * Checks if a student exists with a given name.
	 * @param id is the ID of student to input
	 * @return boolean true if a student with the 
	 * given ID already exists in the roster
	 */
	private boolean idTaken(String id) {
		for (Student item : Server.getStudents()) {
			if (item.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Resets the parent dropdown menu to reflect
	 * any new Students
	 */
	private void resetDropdown() {
		SelectStudentMenu.setItems(Server.getObserableStudentList());
		SelectStudentMenu.getItems().add(0, noParent);
	}
}
