package view.roster;

import javax.swing.JOptionPane;

import model.driver.Grader;
import model.roster.Student;
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
public class DisplayAddStudentPopupEventHandler extends ActionEvent implements EventHandler<ActionEvent> {
	private GradebookController contr;
	private MenuItem callingItem;
	
	/**
	 * Creates an event handler with the given parameters
	 * @param callingItem The MenuItem that triggered the event
	 * @param contr The GradebookController that the dialog will edit
	 */
	public DisplayAddStudentPopupEventHandler(MenuItem callingItem, GradebookController contr) {
		this.contr = contr;
		this.callingItem = callingItem;
	}
	
	/**
	 * Handles the event
	 * @param event The ActionEvent
	 */
	public void handle(ActionEvent event) {
		String name = JOptionPane
				.showInputDialog("Please input a name");
		Grader.addStudent(new Student(name, ""
				+ (name.length() * 236 - name.charAt(0))));
		contr.refresh();
	}
}
