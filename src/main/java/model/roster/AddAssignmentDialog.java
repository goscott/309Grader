package model.roster;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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

	private static GradebookTable parent;

	public void setParent(GradebookTable newParent) {
		parent = newParent;
	}

	public void start(Stage stage) {
		try {
			BorderPane page = (BorderPane) FXMLLoader.load(getClass()
					.getResource("../../view/roster/addAssignmentDialog.fxml"));
			Scene popup = new Scene(page);
			stage.setScene(popup);
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
	private void handleAddButton(ActionEvent event) {
		parent.addAssignmentColumn(nameField.getText(), descrField.getText());
	}

	@FXML
	private void handleCancelButton(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.hide();
	}
}
