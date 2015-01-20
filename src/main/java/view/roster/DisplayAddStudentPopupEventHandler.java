package view.roster;

import javax.swing.JOptionPane;

import model.driver.Grader;
import model.roster.Student;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class DisplayAddStudentPopupEventHandler extends ActionEvent implements EventHandler<ActionEvent> {
	private GradebookController contr;
	private MenuItem callingItem;
	
	public DisplayAddStudentPopupEventHandler(MenuItem callingItem, GradebookController contr) {
		this.contr = contr;
		this.callingItem = callingItem;
	}
	
	public void handle(ActionEvent event) {
		String name = JOptionPane
				.showInputDialog("Please input a name");
		Grader.addStudent(new Student(name, ""
				+ (name.length() * 236 - name.charAt(0))));
		contr.refresh();
	}
}
