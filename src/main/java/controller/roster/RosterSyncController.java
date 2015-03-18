package controller.roster;

import java.util.ArrayList;

import javafx.scene.control.ListCell;
import model.administration.UserTypes;
import model.driver.Grader;
import model.roster.Student;
import model.server.Server;
import controller.Alert;
import controller.GraderPopup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Set;

/**
 * This is the RosterSync Controller 
 * @author Shelli Crispen
 *
 */
public class RosterSyncController
{
    @FXML 
    private ListView<String> syncLocal;
    /** The Servers roster list **/
    @FXML 
    private ListView<String> syncServer;
    /** Button to sync the rosters **/
    @FXML
    private Button syncButton;
    /** The controller for the gradebook **/
    private static GradebookController gbook;
    /** ArrayList of local names to be deleted **/
    private ArrayList<Student> localArray = new ArrayList<Student>();
    /** ArrayList of server names to be added **/
    private ArrayList<Student> serverArray = new ArrayList<Student>();
    /** Spacing used for list View **/
    private static final String SPACING = " %-36s%35s";
    
    /**
     * Initializes the dropdown box.
     */
    public void initialize() {
        gbook = GradebookController.get();
        ObservableList<String> server =  FXCollections.observableArrayList();   
        ObservableList<String> local =  FXCollections.observableArrayList();  
        
        for(Student stu: Grader.getRoster().rosterSync(false))
        {
            serverArray.add(stu);
        	if(stu != null) {
        	    server.add(stu.getName());
        	}
        }
        for(Student stu: Grader.getRoster().rosterSync(true))
        {
            localArray.add(stu);
        	if(stu != null){
                local.add(stu.getName());
        	}
        }   
        for(String s : server) {
        	System.out.println("server " + s);
        }
        syncLocal.setItems(local);
        syncLocal.setOrientation(Orientation.VERTICAL);
        syncServer.setItems(server);
        syncServer.setOrientation(Orientation.VERTICAL);
        
    }
    

    /**
     * Creates the popup on the given stage
     * @param stage the stage
     */
    public void start(Stage stage) {
        GraderPopup.getPopupStage("Roster Sync", "view/roster/RosterSync.fxml")
                .show();
    }

    /**
     * Handles the Sync button. Closes the window.
     * @param event the button's event
     */
    @FXML
    private void handleSyncButton(ActionEvent event) {
        Alert.show("Roster has been synced");
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }
    
}