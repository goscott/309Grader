package controller.roster;

import javax.swing.JOptionPane;

import model.driver.Debug;
import model.driver.Grader;
import model.roster.Student;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.stage.Stage;

/**
 * An ActionEvent that handles displaying the "Add Assignment" dialog.
 * 
 * @author Gavin Scott
 * @author Shelli Crispen
 *
 */
public class CellEditEventHandler implements
		EventHandler<CellEditEvent<Student, String>> {
	/**
	 * Handles the event
	 * 
	 * @param t
	 *            The CellEditEvent
	 */
	public void handle(CellEditEvent<Student, String> t) {
		String input = t.getNewValue();
		try {
			double newGrade = Double.parseDouble(input);
			if (newGrade < 0) {
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
				if (newGrade < 0) {
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
	}
}
