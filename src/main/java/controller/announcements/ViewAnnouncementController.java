package controller.announcements;

import controller.GraderPopup;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.announcements.Announcement;

/**
 * Controls the popup that displays announcements
 * @author Gavin Scott
 */
public class ViewAnnouncementController {
	/** The textArea for showing the body of the announcement **/
	@FXML
	private TextArea bodyField;
	/** The displayed announcement **/
	private static Announcement announcement;

	/**
	 * Sets the announcement to display
	 */
	static void setAnnouncement(Announcement ann) {
		announcement = ann;
	}

	/**
	 * Starts the scene
	 */
	public void start(Stage stg) {
		// "../../view/announcements/viewAnnouncement.fxml"
		Stage stage = GraderPopup.getPopupStage(announcement.getSubject()
				+ " - posted by " + announcement.getPostedBy(),
				"view/announcements/viewAnnouncement.fxml");
		stage.show();
	}

	/**
	 * Initializes the popup
	 */
	public void initialize() {
		bodyField.setText(announcement.getContent());
		bodyField.setEditable(false);
	}
}