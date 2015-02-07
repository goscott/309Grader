package controller.roster;

import java.io.IOException;

import model.driver.Debug;
import model.driver.Grader;
import model.roster.Student;
import model.server.Server;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * controls the Add Student dialog functionality complete
 * @author Shelli Crispen
 * 
 */
public class AddStudentDialogController {
	/** contains the ComboBox with all the students names to add **/
	@FXML
	private ComboBox<String> SelectStudentMenu;
	/** the add button  **/
	@FXML
	private Button addButton;
	/** the cancel button  **/
	@FXML
	private Button cancelButton;
	/** The controller for the gradebook **/
	private static GradebookController gbook;
	/** the MenuItem to set the gradebook to **/
	private static MenuItem parent;
	/** the server that holds all student info **/
	private static Server server;
	
	/**
	 * Sets the parent of the window, so it can grab information from the
	 * gradebook table
	 * 
	 * @param newParent the parent
	 */
	public void setParent(MenuItem newParent, GradebookController gbook) {
		this.gbook = gbook;
		parent = newParent;
		parent.setDisable(true);
	}
	
	
	/**
	 * Initializes the dropdown box. 
	 */
	public void initialize() {
		resetDropdown();
	}
	
	/**
	 * Creates the popup on the given stage
	 * 
	 * @param stage the stage
	 */
	public void start(Stage stage) {
		try {
			BorderPane page = (BorderPane) FXMLLoader.load(getClass()
					.getResource("../../view/roster/AddStudent.fxml"));
			Scene popup = new Scene(page);
			stage.setTitle("Add Student");
			stage.setScene(popup);
			stage.setResizable(false);
			stage.show();
		} catch (IOException ex) {
			Debug.log("IO ERROR", "Could not load file to start popup");
			ex.printStackTrace();
		}

		stage.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				parent.setDisable(false);
			}
		});
	}
	
	@FXML
	/**
	 * Handles the Add button. Closes the window.
	 * @param event the button's event
	 */
	private void handleAddButton(ActionEvent event){
		if (SelectStudentMenu.getValue() != null) {
			Student addS = null;
			for(Student student: server.getObservableStudentList()){
				if(student.getName() == SelectStudentMenu.getValue()){
					addS = student;
				}
			}
					
			Grader.getRoster().addStudent(addS);
		}
		resetDropdown();
		gbook.refresh();
	}
	
	@FXML
	/**
	 * Handles the cancel button. Closes the window.
	 * @param event the button's event
	 */
	private void handleCancelButton(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.hide();
	}
	
	/**
	 * Resets the parent dropdown menu to reflect
	 * that students are added to the gradebook and no longer only in the server
	 */
	private void resetDropdown() {
		ObservableList<String> studentList = Server.getStudentListNameNotRoster();
		if(studentList != null){
			SelectStudentMenu.setItems(studentList);
		}
	}
}
