package controller.announcements;

import controller.GraderPopup;
import model.announcements.Announcement;
import model.driver.Debug;
import model.driver.Grader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controls the popup that adds announcements
 * @author Gavin Scott
 */
public class AddAnnouncementController {
	/** The field for entering the body **/
	@FXML
	private TextArea bodyField;
	/** The field fot entering the subject **/
	@FXML
	private TextField subjectField;
	/** The button that commits the announcement **/
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
		GraderPopup.getPopupStage("Add Announcement",
				"view/announcements/addAnnouncementDialog.fxml").show();
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
			Announcement ann = new Announcement(subjectField.getText(), Grader
					.getUser().getId(), bodyField.getText());
			Grader.getRoster().addAnnouncement(ann);
			AnnouncementsController.refresh();
			bodyField.setText("");
			subjectField.setText("");
		}
	}
}