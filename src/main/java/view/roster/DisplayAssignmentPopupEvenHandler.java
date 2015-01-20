package view.roster;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class DisplayAssignmentPopupEvenHandler extends ActionEvent implements EventHandler<ActionEvent> {
	private GradebookController contr;
	private MenuItem callingItem;
	
	public DisplayAssignmentPopupEvenHandler(MenuItem callingItem, GradebookController contr) {
		this.contr = contr;
		this.callingItem = callingItem;
	}
	
	public void handle(ActionEvent event) {
		Stage newStage = new Stage();
		AddAssignmentDialogController popup = new AddAssignmentDialogController();
		popup.setParent(callingItem, contr);
		popup.start(newStage);
	}
}
