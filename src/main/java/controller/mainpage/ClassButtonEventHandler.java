package controller.mainpage;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

import controller.Alert;
import controller.announcements.AnnouncementsController;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Roster;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * 
 * handles the event of a class button being pressed
 * 
 * @author Michael Lenz
 *
 */
public class ClassButtonEventHandler implements EventHandler<ActionEvent> {

	/**
	 * handles the event of a class button being pressed by attempting to load
	 * the roster.
	 */
	@Override
	public void handle(ActionEvent event) {
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
							.equals(rost.courseName())) {
				Action response = Alert
						.showConfirmDialog("Would you like to save the current roster?");
				if (response == Dialog.ACTION_YES) {
					Roster.save(Grader.getRoster());
				}
			}
			Grader.setCurrentRoster(rost);
			MainPageController.enableTabs();
			AnnouncementsController.refresh();
			
		}

	}
}
