package model.roster;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.driver.Grader;
import javafx.application.Application;
import javafx.beans.property.Property;
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
import javafx.scene.control.MenuButton;
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

public class AddAssignmentDialog {

	@FXML
	private Button cancelButton;
	@FXML
	private TextField nameField;
	@FXML
	private Button addButton;
	@FXML
	private TextArea descrField;
	@FXML
	private MenuButton parentDropDown;

	private static GradebookTable parent;
	private final int maxChars = 25;
	private final int buffer = 5;

	public void setParent(GradebookTable newParent) {
		parent = newParent;
	}

	public void start(Stage stage) {
		try {
			BorderPane page = (BorderPane) FXMLLoader.load(getClass()
					.getResource("../../view/roster/addAssignmentDialog.fxml"));
			Scene popup = new Scene(page);
			stage.setTitle("Add Assignment");
			stage.setScene(popup);
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		stage.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				parent.asgnButton.setDisable(false);
			}
		});/*
			 * System.out.println("namefield: " + nameField);
			 * System.out.println("namefield.textProperty(): " +
			 * nameField.textProperty());
			 * nameField.textProperty().addListener(new ChangeListener<String>()
			 * { public void changed(final ObservableValue<? extends String>
			 * observable, final String oldValue, final String newValue) {
			 * System.out.println("text: " + nameField.getText());
			 * /*if(nameField.getText().length() > maxChars) {
			 * addButton.setDisable(true); } else { addButton.setDisable(false);
			 * } } });
			 */
	}

	@FXML
	private void handleAddButton(ActionEvent event) {
		parent.addAssignmentColumn(nameField.getText(), descrField.getText());
		nameField.setText("");
		descrField.setText("");
	}

	@FXML
	private void handleCancelButton(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.hide();
	}

	@FXML
	// check name for validity, informs user if wrong
	private void nameChangeHandler(KeyEvent event) {
		Tooltip tooltip = new Tooltip();
		// INVALID LENGTH
		if (nameField.getText().length() > maxChars) {
			addButton.setDisable(true);
			nameField.setBackground(new Background(new BackgroundFill(
					Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
			// make tooltip
			tooltip.setText("Assignment names must not exceed " + maxChars + " characters");
			tooltip.setAutoHide(true);
			tooltip.setAutoFix(true);
			Node source = (Node) event.getSource();
			Stage stage = (Stage)source.getScene().getWindow();
			tooltip.show(source, 
					stage.getX() + nameField.getLayoutX() + 53, 
					stage.getY() + 2*nameField.getLayoutY() + 37);
		// UNIQUE NAMES
		} else if(nameTaken(nameField.getText())) {
			addButton.setDisable(true);
			nameField.setBackground(new Background(new BackgroundFill(
					Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
			// make tooltip
			tooltip.setText("Assignment names must be unique");
			tooltip.setAutoHide(true);
			tooltip.setAutoFix(true);
			Node source = (Node) event.getSource();
			Stage stage = (Stage)source.getScene().getWindow();
			tooltip.show(source, 
					stage.getX() + nameField.getLayoutX() + 53, 
					stage.getY() + 2*nameField.getLayoutY() + 37);
		} else {
			tooltip.hide();
			addButton.setDisable(false);
			nameField.setStyle("-fx-background: #FFFFFF;");
			nameField.setBackground(new Background(new BackgroundFill(
					Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		}
	}
	
	private boolean nameTaken(String name) {
		for(GradedItem item : Grader.getRoster().getAssignments()) {
			if(item.name().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
