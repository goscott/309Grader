package view.roster;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * An ActionEvent that handles displaying the
 * "Add Assignment" dialog.
 * @author Gavin Scott
 *
 */
public class DisplayAddAssignmentPopupEventHandler extends ActionEvent implements EventHandler<ActionEvent> {
	private GradebookController contr;
	private MenuItem callingItem;
	
	/**
	 * Creates an event handler with the given parameters
	 * @param callingItem The MenuItem that triggered the event
	 * @param contr The GradebookController that the dialog will edit
	 */
	public DisplayAddAssignmentPopupEventHandler(MenuItem callingItem, GradebookController contr) {
		this.contr = contr;
		this.callingItem = callingItem;
	}
	
	/**
	 * Handles the event
	 * @param event The ActionEvent
	 */
	public void handle(ActionEvent event) {
		Stage newStage = new Stage();
		AddAssignmentDialogController popup = new AddAssignmentDialogController();
		popup.setParent(callingItem, contr);
		popup.start(newStage);
	}
}
