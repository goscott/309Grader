package controller.roster;

import java.io.IOException;

import controller.GraderPopup;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Student;
import model.server.Server;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * controls the Add Student dialog functionality complete
 * 
 * @author Shelli Crispen
 * 
 */
public class AddStudentDialogController {
	/** contains the ComboBox with all the students names to add **/
	@FXML
	private ComboBox<String> SelectStudentMenu;
	/** the add button **/
	@FXML
	private Button addButton;
	/** the cancel button **/
	@FXML
	private Button cancelButton;
	/** The controller for the gradebook **/
	private static GradebookController gbook;

	/**
	 * Initializes the dropdown box.
	 */
	public void initialize() {
		gbook = GradebookController.get();
		resetDropdown();
	}

	/**
	 * Creates the popup on the given stage
	 * 
	 * @param stage
	 *            the stage
	 */
	public void start(Stage stage) {
		GraderPopup.getPopupStage("Add Student", "view/roster/AddStudent.fxml").show();
	}

	@FXML
	/**
	 * Handles the Add button. Closes the window.
	 * @param event the button's event
	 */
	private void handleAddButton(ActionEvent event) {
		if (SelectStudentMenu.getValue() != null) {
			Student addS = null;
			for (Student student : Server.getObservableStudentList()) {
				if (student.getName() == SelectStudentMenu.getValue()) {
					addS = student;
				}
			}
			GradebookController.edited = true;
			Grader.getRoster().addStudent(addS);
		}
		resetDropdown();
		gbook.refresh();
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
	 * Resets the parent dropdown menu to reflect that students are added to the
	 * gradebook and no longer only in the server
	 */
	private void resetDropdown() {
		ObservableList<String> studentList = Server
				.getStudentListNameNotRoster();
		if (studentList != null) {
			SelectStudentMenu.setItems(studentList);
		}
	}
}
