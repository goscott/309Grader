package controller.roster;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

import resources.ResourceLoader;
import controller.GraderPopup;
import controller.mainpage.ClassButtonsController;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Roster;
import model.server.Server;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * controls the Add class dialog functionality incomplete
 * 
 * @author Michael Lenz
 * 
 */
public class AddClassDialogController {

	/** contains the resource bundle **/
	@FXML
	private ResourceBundle resources;
	/** the location of the main fxml file **/
	@FXML
	private URL location;
	/** the cancel button **/
	@FXML
	private Button CancelButton;
	/** the viewable list of students **/
	@FXML
	private ListView<String> students;
	/** Text field to enter the class name **/
	@FXML
	private TextField className;
	/** Text field to enter the Section Number **/
	@FXML
	private TextField sectionNumber;
	/** button to add the class **/
	@FXML
	private Button AddClassButton;

	@FXML
	private DatePicker StartDate;

	@FXML
	private MenuButton quarter;

	/**
	 * initializes and displays a new
	 */
	@FXML
	void initialize() {
		students.setItems(Server.getStudentListName());
		students.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		this.StartDate.setValue(LocalDate.now());
		AddClassButton.setDisable(true);
		
		quarter.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				AddClassButton.setDisable(!isValid());
			}
		});
		
		sectionNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				AddClassButton.setDisable(!isValid());
				if(!isSectionValid()) {
					sectionNumber.setBackground(new Background(new BackgroundFill(
							ResourceLoader.ERROR_RED, CornerRadii.EMPTY, Insets.EMPTY)));
				} else {
					sectionNumber.setBackground(new Background(new BackgroundFill(
							ResourceLoader.NOERROR_WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				}
			}
		});
	}
	
	public void springSelected() {
	    quarter.setText("Spring");
	}
	
	public void summerSelected() {
	    quarter.setText("Summer");
	}
	
	public void fallSelected() {
	    quarter.setText("Fall");
	}
	
	public void winterSelected() {
	    quarter.setText("Winter");
	}

	/**
	 * Handels the event of when the addClass button is pressed
	 * 
	 * @param event
	 *            event to be handeled
	 */
	@FXML
	private void AddClass(ActionEvent event) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(java.sql.Date.valueOf(StartDate.getValue()));
		Roster roster = null;
		try {
			roster = new Roster(className.getText(), Grader.getUser().getId(),
					Integer.parseInt(sectionNumber.getText()),
					quarter.getText(), cal, Calendar.getInstance());
		} catch (NumberFormatException ex) {
			sectionNumber
					.setBackground(new Background(new BackgroundFill(
							ResourceLoader.ERROR_RED, CornerRadii.EMPTY,
							Insets.EMPTY)));
			return;
		}
		for (String name : students.getSelectionModel().getSelectedItems()) {
			roster.addStudent(Server.getStudentByName(name));
		}
		roster.Save();
		Debug.log("Roster created and saved");
		ClassButtonsController.refresh();
		((Stage) AddClassButton.getScene().getWindow()).close();
	}

	private boolean isValid() {
		return isSectionValid() 
				&& !quarter.getText().equals("Select a Quarter")
				&& className.getText().trim().length() > 0;
	}

	private boolean isSectionValid() {
		try {
			Integer.parseInt(sectionNumber.getText());
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * cances and closes the dialog
	 * 
	 * @param event
	 */
	@FXML
	void Cancel(ActionEvent event) {
		((Stage) AddClassButton.getScene().getWindow()).close();
	}

	/**
	 * starts the add class dialog
	 * 
	 * @param stage
	 * @param superClass
	 */
	public void start(Stage stg, ClassButtonsController superClass) {
		GraderPopup.getPopupStage("Add Class",
				"view/roster/addClassDialog.fxml").show();
	}

}