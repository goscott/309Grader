package model.roster;

import java.io.IOException;

import model.driver.Grader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * The dialog that allows the user to add assignments to the gradebook
 * 
 * @author Gavin Scott
 *
 */
public class AddAssignmentDialog {
	@FXML
	private TextField nameField;
	@FXML
	private Button addButton;
	@FXML
	private TextArea descrField;
	@FXML
	private ComboBox<String> parentDropdown;
	@FXML
	private Button refreshButton;

	private static GradebookTable parent;
	private final int maxChars = 25;
	private final int numParentsShown = 10;
	private final String noParent = "<None>";

	/**
	 * Sets the parent of the window, so it can grab information from the
	 * gradebook table
	 * 
	 * @param newParent
	 *            the parent
	 */
	public void setParent(GradebookTable newParent) {
		parent = newParent;
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
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		stage.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				parent.asgnButton.setDisable(false);
			}
		});
	}

	@FXML
	// adds a new assignment
	private void handleAddButton(ActionEvent event) {
		GradedItem asgnParent = null;
		System.out.println("SELECTED PARENT: " + parentDropdown.getValue());
		if(parentDropdown.getValue() != null && !parentDropdown.getValue().equals(noParent)) {
			asgnParent = Grader.getRoster().getAssignment(parentDropdown.getValue());
		}
		parent.addAssignmentColumn(nameField.getText(), descrField.getText(), asgnParent);
		nameField.setText("");
		descrField.setText("");
		resetDropdown();
	}

	@FXML
	// close the window without adding a student
	private void handleCancelButton(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.hide();
	}

	@FXML
	// check name for validity, informs user if wrong
	private void nameChangeHandler(KeyEvent event) {
		Tooltip tooltip = new Tooltip();

		if (nameField.getText().length() > maxChars) {
			// ENFORCE MAXIMUM LENGTH
			addButton.setDisable(true);
			nameField.setBackground(new Background(new BackgroundFill(
					Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
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
			addButton.setDisable(true);
			nameField.setBackground(new Background(new BackgroundFill(
					Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
			// make tooltip
			tooltip.setText("Assignment names must be unique");
			tooltip.setAutoHide(true);
			tooltip.setAutoFix(true);
			Node source = (Node) event.getSource();
			Stage stage = (Stage) source.getScene().getWindow();
			tooltip.show(source, stage.getX() + nameField.getLayoutX() + 53,
					stage.getY() + 2 * nameField.getLayoutY() + 37);

		} else {
			addButton.setDisable(false);
			tooltip.hide();
			nameField.setStyle("-fx-background: #FFFFFF;");
			nameField.setBackground(new Background(new BackgroundFill(
					Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		}

		// NOT TOO SHORT
		if (nameField.getText().length() == 0) {
			addButton.setDisable(true);
		}
	}

	// checks if an assignment already exists with that name
	private boolean nameTaken(String name) {
		for (GradedItem item : Grader.getRoster().getAssignments()) {
			if (item.name().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private void resetDropdown() {
		parentDropdown.setItems(Grader.getAssignmentNameList());
		parentDropdown.getItems().add(0, noParent);
		parentDropdown.setVisibleRowCount(numParentsShown);
	}
	
	@FXML
	private void handleRefreshButton(ActionEvent event) {
		resetDropdown();
	}
}
