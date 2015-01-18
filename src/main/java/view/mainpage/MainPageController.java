package view.mainpage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import view.roster.AddAssignmentDialogController;
import view.roster.GradebookController;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import model.administration.User;
import model.administration.UserDB;
import model.administration.UserTypes;
import model.administration.tests.PermissionsTester;
import model.driver.Driver;
import model.driver.Grader;

public class MainPageController {
	@FXML
	private TabPane tabPane;
	@FXML
    private Tab classTab;
	@FXML
    private Tab gradebookTab;
	@FXML
    private Tab graphsTab;
	@FXML
    private Tab historyTab;
	@FXML
    private Tab predictionsTab;
	@FXML
    private Tab announcmentsTab;
	@FXML
	private Menu classMenu;
	@FXML
	private MenuItem addAssignment;
	@FXML
	private MenuItem dropAssignment;
	@FXML
	private MenuItem importAssignment;
	@FXML
	private MenuItem addStudent;
	@FXML
	private MenuItem dropStudent;
	@FXML
	private MenuItem rosterSync;
	
	public void initialize() {
		// gives it some initial data
		Driver.test();
		
		// loads tab contents
		try {
			// add gradebook
			TableView<?> gradebookPage = (TableView<?>) FXMLLoader.load(getClass().getResource("../roster/gradebook_screen.fxml"));
			gradebookTab.setContent(gradebookPage);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// FILE MENU
		
		// disable class menu if no current roster
		classMenu.disableProperty().bind(new BooleanBinding() {
		    @Override
		    protected boolean computeValue() {
		        return Grader.getRoster() == null;
		    }
		});
		EventHandler<ActionEvent> action = handleMenuItems();
		addAssignment.setOnAction(action);
		
	}
	
	@FXML
    //launches the permissions editor
    public void permissions() {
        System.out.println("Launching permissions editor");
        UserDB users = new UserDB();
        
        try {
            Stage stage = new Stage();
            
            //BorderPane page = (BorderPane) FXMLLoader.load(getClass().getResource("PermissionsEditor.fxml"));
            Scene scene = new Scene((Parent) FXMLLoader.load(getClass().getResource("../administration/permissions_editor.fxml")));
            
            ListView<String> view = (ListView<String>) scene.lookup("#user_list");
            ObservableList<String> list = FXCollections.observableArrayList();
            
            for (User target : users.getUsers()) {
                list.add(String.format("%-40s%51s", target.getId(), UserTypes.fullName(target.getType())));
            }
            
            view.setItems(list);
            
            stage.setScene(scene);
            stage.setTitle("Edit User Permissions");
            stage.setResizable(false);
            
            stage.show();
            
        } catch (Exception ex) {
            Logger.getLogger(PermissionsTester.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("ERR");
        }
    }
	
	private EventHandler<ActionEvent> handleMenuItems() {
        return new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                MenuItem mItem = (MenuItem) event.getSource();
                switch(mItem.getText()) {
                case "Add Assignment":
                	Stage newStage = new Stage();
            		AddAssignmentDialogController popup = new AddAssignmentDialogController();
            		popup.setParent(mItem);
            		popup.start(newStage);
                	break;
                }
            }
        };
    }
}
