package controller.roster;

import model.driver.Debug;
import model.driver.Grader;
import model.roster.Student;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellEditEvent;

/**
 * An ActionEvent that handles displaying the "Add Assignment" dialog.
 * 
 * @author Gavin Scott
 *
 */
public class CellEditEventHandler implements
		EventHandler<CellEditEvent<Student, String>> {
	/** The controller for the gradebook **/
	private GradebookController controller;
	
	/**
	 * Initializes an event, with the given controller
	 * @param ctrl the gradebook's controller
	 */
	public CellEditEventHandler(GradebookController ctrl) {
		controller = ctrl;
	}
	
	/**
	 * Handles the event
	 * 
	 * @param t
	 *            The CellEditEvent
	 */
	public void handle(CellEditEvent<Student, String> t) {
		String input = t.getNewValue();
		if(input.trim().length() == 0) {
			Grader.addScore(t.getRowValue(), t.getTableColumn().getText(),
					null);
			return;
		}
		try {
			double newGrade = Double.parseDouble(input);
			double maxScore = Grader.getAssignment(t.getTableColumn().getText()).maxScore();
			if (newGrade < 0 || newGrade > maxScore) {
				throw new NumberFormatException();
			}
			Grader.addScore(t.getRowValue(), t.getTableColumn().getText(),
					newGrade);
		} catch (NumberFormatException ex) {
			try {
				if(input.indexOf('%') != input.length() - 1) {
					throw new NumberFormatException();
				}
				input = input.substring(0, input.length() - 1);
				double newGrade = Double.parseDouble(input);
				if (newGrade < 0 || newGrade > 100) {
					throw new NumberFormatException();
				}
				Grader.addPercentageScore(t.getRowValue(), t.getTableColumn().getText(),
						newGrade);
				
			} catch (NumberFormatException ex2) {
				Tooltip tip = new Tooltip("Invalid input");
				tip.setAutoFix(true);
				tip.setAutoHide(true);
				tip.show(t.getTableView().getScene().getWindow());
				Debug.log("User Input Error", "Entered invalid grade");
			}
		}
		controller.refresh();
	}
}
