package controller.announcements;

import java.io.IOException;

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
	public void start(Stage stage) {
		try {
			Pane page = (Pane) FXMLLoader.load(getClass().getResource(
					"../../view/announcements/viewAnnouncement.fxml"));
			Scene popup = new Scene(page);
			stage.setTitle(announcement.getSubject() + " - posted by "
					+ announcement.getPostedBy());
			stage.setScene(popup);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e1) {
			Debug.log("IO ERROR", "Could not load file to start popup");
			e1.printStackTrace();
		}
	}

	public void initialize() {
		bodyField.setText(announcement.getContent());
		bodyField.setEditable(false);
	}
}