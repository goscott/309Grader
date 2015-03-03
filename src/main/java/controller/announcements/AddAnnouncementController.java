package controller.announcements;

import java.io.IOException;

import controller.GraderPopup;
import model.announcements.Announcement;
import model.driver.Debug;
import model.driver.Grader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AddAnnouncementController {
	@FXML
	private TextArea bodyField;
	@FXML
	private TextField subjectField;
	@FXML
	private Button sendButton;

	/**
	 * Initializes the popup
	 */
	public void initialize() {
		sendButton.setDisable(true);
		subjectField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				sendButton.setDisable(bodyField.getText().length() == 0
						|| subjectField.getText().length() == 0);
			}
		});
		bodyField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				sendButton.setDisable(bodyField.getText().length() == 0
						|| subjectField.getText().length() == 0);
			}
		});
	}
	
	/**
	 * Starts the scene
	 */
	public void start(Stage stage) {
		try {
			Pane page = (Pane) FXMLLoader.load(getClass().getClassLoader()
					.getResource("view/announcements/addAnnouncementDialog.fxml"));
			Scene popup = new Scene(page);
			stage.setTitle("Add Announement");
			stage.setScene(popup);
			stage.setResizable(false);
			GraderPopup.setIcon(stage);
			stage.show();
		} catch (IOException e1) {
			Debug.log("IO ERROR", "Could not load file to start popup");
			e1.printStackTrace();
		}
	}

	/**
	 * Handles the press of the send button. Creates an announcement from the
	 * data in the fields and adds it to the server.
	 */
	@FXML
	private void handleButtonPress(ActionEvent e) {
		if (bodyField.getText().length() > 0
				&& subjectField.getText().length() > 0) {
			Debug.log("Announcement Added", subjectField.getText() + " : "
					+ bodyField.getText());
			Announcement ann = new Announcement(subjectField.getText(), Grader.getUser().getId(),
					bodyField.getText());
			Grader.getRoster().addAnnouncement(ann);
			AnnouncementsController.refresh();
			bodyField.setText("");
			subjectField.setText("");
		}
	}
}