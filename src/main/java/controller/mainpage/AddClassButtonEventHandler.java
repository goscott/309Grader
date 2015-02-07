package controller.mainpage;

import controller.roster.AddClassDialogController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * handles the addClass button event
 * 
 * @author Michael Lenz
 *
 */
public class AddClassButtonEventHandler implements EventHandler<ActionEvent> {

	private ClassButtonsController superClass;

	public AddClassButtonEventHandler(
			ClassButtonsController classButtonsController) {
		superClass = classButtonsController;
	}

	/**
	 * creates a new class dialog when the button is pressed.
	 */
	@Override
	public void handle(ActionEvent event) {
		new AddClassDialogController().start(new Stage(), superClass);
		((Stage) ((Button) event.getSource()).getScene().getWindow())
				.setTitle("Grader Tool : "
						+ ((Button) event.getSource()).getText());
		superClass.refreshButtons();
	}
}
