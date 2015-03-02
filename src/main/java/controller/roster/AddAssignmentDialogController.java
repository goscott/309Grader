package controller.roster;

import java.io.IOException;

import resources.ResourceLoader;
import controller.GraderPopup;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.GradedItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The dialog that allows the user to add assignments to the gradebook
 * 
 * @author Gavin Scott
 *
 */
public class AddAssignmentDialogController {
	/** The field for the assignment's name **/
	@FXML
	private TextField nameField;
	/** The add button **/
	@FXML
	private Button addButton;
	/** The text area for entering the description **/
	@FXML
	private TextArea descrField;
	/** The dropdown that let's the user choose a parent assignment **/
	@FXML
	private ComboBox<String> parentDropdown;
	/** The refresh button **/
	@FXML
	private Button refreshButton;
	/** The textfield for entering the maximum score **/
	@FXML
	private TextField maxScoreField;
	/** The checkbox determining if an assignment is extra credit **/
	@FXML
	private CheckBox ecBox;

	/** The controller for the gradebook **/
	private static GradebookController gbook;
	/** The MenuItem that was clicked to display the window **/
	private static MenuItem parent;
	/** The max number of characters allowed in a name **/
	private final int maxChars = 25;
	/** Number of parents shown before a scrollbar appears **/
	private final int numParentsShown = 10;
	/** The string displayed that lets the user choose no parent **/
	private final String noParent = "<None>";

	/**
	 * Sets the parent of the window, so it can grab information from the
	 * gradebook table
	 * 
	 * @param newParent
	 *            the parent
	 */
	@SuppressWarnings("static-access")
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
		addButton.setDisable(true);
		// populates parent list
		resetDropdown();
		parentDropdown.focusedProperty().addListener(
				new ChangeListener<Boolean>() {
					@Override
					public void changed(
							ObservableValue<? extends Boolean> arg0,
							Boolean oldPropertyValue, Boolean newPropertyValue) {
						if (newPropertyValue) {
							resetDropdown();
						}
					}
				});
		maxScoreField.setText("100");
	}

	/**
	 * Creates the popup on the given stage
	 * 
	 * @param stage
	 *            the stage
	 */
	public void start(Stage stage) {
		try {
			BorderPane page = (BorderPane) FXMLLoader.load(getClass()
					.getResource("../../view/roster/addAssignmentDialog.fxml"));
			Scene popup = new Scene(page);
			stage.setTitle("Add Assignment");
			stage.setScene(popup);
			stage.setResizable(false);
			GraderPopup.setIcon(stage);
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
	 * Handles the add button. Adds a new assignment and
	 * clears the fields in the dialog.
	 * @param event the button's actionevent
	 */
	private void handleAddButton(ActionEvent event) {
		GradedItem asgnParent = null;
		if (parentDropdown.getValue() != null
				&& !parentDropdown.getValue().equals(noParent)) {
			asgnParent = Grader.getRoster().getAssignment(
					parentDropdown.getValue());
		}
		GradedItem item = new GradedItem(nameField.getText(),
				descrField.getText(), Double.parseDouble(maxScoreField
						.getText()), asgnParent, ecBox.isSelected());
		Grader.addAssignment(item);
		gbook.setAssignmentExpansion(item.name(), true);
		nameField.setText("");
		descrField.setText("");
		resetDropdown();
		if(asgnParent != null && asgnParent.numChildren() == 1) {
			gbook.fullRefresh();
		}
		else {
			gbook.refresh();
		}
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

	@FXML
	/**
	 * Checks the name for validity every time the user
	 * changes it. Names must be unique and between 1 and
	 * a maximum number of characters.
	 * @param event The key event
	 */
	private void nameChangeHandler(KeyEvent event) {
		addButton.setDisable(checkValid());
		if (nameField.getText().length() == 0) {
			nameField.setBackground(new Background(new BackgroundFill(
					ResourceLoader.ERROR_RED, CornerRadii.EMPTY, Insets.EMPTY)));
		}
		Tooltip tooltip = new Tooltip();
		if (nameField.getText().length() > maxChars) {
			// make tooltip
			tooltip.setText("Assignment names must not exceed " + maxChars
					+ " characters");
			tooltip.setAutoHide(true);
			tooltip.setAutoFix(true);
			Node source = (Node) event.getSource();
			Stage stage = (Stage) source.getScene().getWindow();
			tooltip.show(source, stage.getX() + nameField.getLayoutX() + 53,
					stage.getY() + 2 * nameField.getLayoutY() + 37);

		} else if (nameTaken(nameField.getText())) {
			// NEED UNIQUE NAMES
			// make tooltip
			tooltip.setText("Assignment names must be unique");
			tooltip.setAutoHide(true);
			tooltip.setAutoFix(true);
			Node source = (Node) event.getSource();
			Stage stage = (Stage) source.getScene().getWindow();
			tooltip.show(source, stage.getX() + nameField.getLayoutX() + 53,
					stage.getY() + 2 * nameField.getLayoutY() + 37);

		} else {
			tooltip.hide();
		}
	}

	@FXML
	/**
	 * Checks if the entered data is valid
	 * @return boolean true if the data is valid
	 */
	private boolean checkValid() {
		checkNameValid();
		checkScoreInvalid();
		return checkNameValid() || checkScoreInvalid() || nameField.getText().length() == 0;
	}
	
	/**
	 * Checks if the entered name is valid. Valid names must be
	 * under 25 characters, and unique.
	 * @return boolean true if the name is valid
	 */
	private boolean checkNameValid() {
		if (nameTaken(nameField.getText())
				|| nameField.getText().length() > maxChars) {
			nameField.setBackground(new Background(new BackgroundFill(
					ResourceLoader.ERROR_RED, CornerRadii.EMPTY, Insets.EMPTY)));
			return true;
		}
		nameField.setStyle("-fx-background: #FFFFFF;");
		nameField.setBackground(new Background(new BackgroundFill(
				ResourceLoader.NOERROR_WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		return false;
	}
	
	/**
	 * Checks if the max score field is invalid
	 * @return boolean true if the data is invalid
	 */
	private boolean checkScoreInvalid() {
		if (maxScoreField.getText().length() == 0){
				//|| Double.parseDouble(maxScoreField.getText()) > Grader.getAssignment(parentDropdown.getValue()).maxScore()) {
			maxScoreField.setBackground(new Background(new BackgroundFill(
					ResourceLoader.ERROR_RED, CornerRadii.EMPTY, Insets.EMPTY)));
			return true;
		}
		try {
			Double.parseDouble(maxScoreField.getText());
			maxScoreField.setStyle("-fx-background: #FFFFFF;");
			maxScoreField.setBackground(new Background(new BackgroundFill(
					ResourceLoader.NOERROR_WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		} catch (NumberFormatException ex) {
			maxScoreField.setBackground(new Background(new BackgroundFill(
					ResourceLoader.ERROR_RED, CornerRadii.EMPTY, Insets.EMPTY)));
			return true;
		}
		return false;
	}

	@FXML
	/**
	 * Checks the score for validity every time the user
	 * changes it. Max scores must be greater than 1
	 * and valid doubles.
	 * @param event The key event
	 */
	private void scoreChangeHandler(KeyEvent event) {
		addButton.setDisable(checkValid());
	}

	/**
	 * Checks if an assignment exists with a given name.
	 * 
	 * @param name
	 *            The name
	 * @return boolean true if an assignment with the given name already exists
	 *         in the roster
	 */
	private boolean nameTaken(String name) {
		for (GradedItem item : Grader.getRoster().getAssignments()) {
			if (item.name().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Resets the parent dropdown menu to reflect any new assignments
	 */
	private void resetDropdown() {
		/*GradedItem curChoice = Grader.getRoster().getAssignment(
				parentDropdown.getValue());*/
		String curChoice = parentDropdown.getValue();
		parentDropdown.setItems(Grader.getAssignmentNameList());
		parentDropdown.getItems().add(0, noParent);
		parentDropdown.setVisibleRowCount(numParentsShown);
		parentDropdown.setValue(curChoice);
	}
}
