package controller.announcements;

import java.io.IOException;

import controller.GraderPopup;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.announcements.Announcement;
import model.driver.Debug;

public class ViewAnnouncementController {
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

	public void initialize() {
		bodyField.setText(announcement.getContent());
		bodyField.setEditable(false);
	}
}