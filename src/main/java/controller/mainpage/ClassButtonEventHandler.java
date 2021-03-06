package controller.mainpage;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

import controller.Alert;
import controller.announcements.AnnouncementsController;
import controller.graph.Histogram;
import controller.roster.GradebookController;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Roster;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * 
 * handles the event of a class button being pressed
 * 
 * @author Michael Lenz
 * @author Gavin Scott
 */
public class ClassButtonEventHandler implements EventHandler<MouseEvent> {

	/**
	 * handles the event of a class button being pressed by attempting to load
	 * the roster.
	 */
	@Override
	public void handle(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY && event.isControlDown()) {
			Action response = Alert.showWarningQuestion(
					"Warning: Class deletion will be permanent",
					"Are you sure you want to delete '" + ((Button)event.getSource()).getText() + "?");
			if (response == Dialog.ACTION_YES) {
				try {
					if(((Button)event.getSource()).getText().equals(
							Grader.getRoster().courseName() + '-' + 
							String.format("%02d", Grader.getRoster().getSection()))) {
						MainPageController.deselectRoster();
					}
					Files.deleteIfExists(FileSystems.getDefault().getPath("Rosters/" + ((Button)event.getSource()).getText() + ".rost"));
				} catch (IOException ex) {
					ex.printStackTrace();
					Debug.log("Error", "Could not delete " + ((Button)event.getSource()).getText());
				}
				ClassButtonsController.refresh();
			}
		} else {
			Debug.log("class button clicked "
					+ ((Button) event.getSource()).getText());
			((Stage) ((Button) event.getSource()).getScene().getWindow())
					.setTitle("Grader Tool : "
							+ ((Button) event.getSource()).getText());
			Roster rost = Roster.load("Rosters/"
					+ ((Button) event.getSource()).getText() + ".rost");
			if (rost != null) {
				if (!Grader.getRoster().courseName().equals(Debug.DEFAULT_NAME)
						&& Grader.getRoster() != null
						&& !Grader.getRoster().courseName()
								.equals(rost.courseName())
						&& GradebookController.edited
						&& !GradebookController.predictionMode
						&& MainPageController.isEnabled()) {
					Action response = Alert
							.showConfirmDialog("Would you like to save the current roster?");
					if (response == Dialog.ACTION_YES) {
						Roster.save(Grader.getRoster());
					}
				}
				Grader.setCurrentRoster(rost);
				MainPageController.enableTabs();
				AnnouncementsController.refresh();
				GradebookController.get().fullRefresh();
				GradebookController.edited = false;
			}
		}

	}
}
