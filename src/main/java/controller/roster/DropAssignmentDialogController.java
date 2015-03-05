package controller.roster;

import java.io.IOException;

import controller.GraderPopup;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.GradedItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The dialog that allows the user to drop assignments from the gradebook
 * 
 * @author Shelli Crispen
 *
 */
public class DropAssignmentDialogController {
	/** The drop button **/
	@FXML
	private Button dropButton;

	/** The cancel button **/
	@FXML
	private Button cancelButton;

	/** The dropdown that let's the user choose a parent assignment **/
	@FXML
	private ComboBox<String> dropAssignmentSelect;

	/** The controller for the gradebook **/
	private static GradebookController gbook;
	/** Number of parents shown before a scrollbar appears **/
	private final int numParentsShown = 10;
	/** The string displayed that lets the user choose no parent **/
	private final String noParent = "<None>";

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
		GraderPopup.getPopupStage("Drop Assignment",
				"view/roster/dropAssignment.fxml").show();
	}

	@FXML
	/**
	 * Handles the cancel button. Closes the window.
	 * @param event the button's event
	 */
	private void handleDropButton(ActionEvent event) {
		if (dropAssignmentSelect.getValue() != null) {
			GradedItem dropS = null;
			for (GradedItem assignment : Grader.getRoster().getAssignments()) {
				if (assignment.name() == dropAssignmentSelect.getValue()) {
					dropS = assignment;
				}
			}
			GradebookController.edited = true;
			Grader.getRoster().dropAssignment(dropS);
		}
		resetDropdown();
		gbook.fullRefresh();
		gbook.populateTree();
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
	 * Resets the parent dropdown menu to reflect any new assignments
	 */
	private void resetDropdown() {
		/*GradedItem curChoice = Grader.getRoster().getAssignment(
		        parentDropdown.getValue());*/
		String curChoice = dropAssignmentSelect.getValue();
		dropAssignmentSelect.setItems(Grader.getAssignmentNameList());
		dropAssignmentSelect.getItems().add(0, noParent);
		dropAssignmentSelect.setVisibleRowCount(numParentsShown);
		dropAssignmentSelect.setValue(curChoice);
	}
}
