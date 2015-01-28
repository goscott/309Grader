package controller.roster;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import model.driver.Debug;
import model.roster.Roster;
import model.server.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button CancelButton;

	@FXML
	private ListView<String> students;

	@FXML
	private TextField className;

	@FXML
	private Button AddClassButton;

	@FXML
	void initialize() {
		ObservableList<String> items = FXCollections
				.observableArrayList("There will be students here someday");
		students.setItems(Server.getObserableStudentList());
		students.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// students.setItems(items);
	}

	@FXML
	private void AddClass(ActionEvent event) {
		Roster roster = new Roster(className.getText(), null, 0, null, null,
				null);
		roster.Save();
		Debug.log("roster saved");
		((Stage) AddClassButton.getScene().getWindow()).close();
	}

	@FXML
	void Cancel(ActionEvent event) {
		((Stage) AddClassButton.getScene().getWindow()).close();
	}

	/**
	 * starts the add class dialog
	 * 
	 * @param stage
	 */
	public void start(Stage stage) {

		try {
			Pane page = (Pane) FXMLLoader.load(getClass().getClassLoader()
					.getResource(("view/roster/addClassDialog.fxml")));
			Scene popup = new Scene(page);
			stage.setTitle("Add Class");
			stage.setScene(popup);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e1) {
			Debug.log("IO ERROR", "Could not load file to start popup");
			e1.printStackTrace();
		}

		stage.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				// parent.setDisable(false);
			}
		});
	}

}