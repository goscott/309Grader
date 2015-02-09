package controller.roster;

import java.io.IOException;

import model.driver.Debug;
import model.driver.Grader;
import model.roster.GradedItem;
import model.roster.Roster;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The dialog that allows the user to drop assignments from the gradebook
 * 
 * @author Shelli Crispen 
 *
 */
public class DropAssignmentDialogController
{
    /** The drop button **/
    @FXML
    private Button dropButton;
    
    /** The cancel button **/
    @FXML
    private Button cancelButton;
    
    /** The dropdown that let's the user choose a parent assignment **/
    @FXML
    private ComboBox<String> dropAssignmentSelect;

    /** The controller for the gradebook **/
    private static GradebookController gbook;
    /** The MenuItem that was clicked to display the window **/
    private static MenuItem parent;
    /** Number of parents shown before a scrollbar appears **/
    private final int numParentsShown = 10;
    /** The string displayed that lets the user choose no parent **/
    private final String noParent = "<None>";
    /** The roster */
    private static Roster roster;
    
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
        	AnchorPane page = (AnchorPane) FXMLLoader.load(getClass()
                    .getResource("../../view/roster/dropAssignment.fxml"));
            Scene popup = new Scene(page);
            stage.setTitle("Drop Assignment");
            stage.setScene(popup);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e1) {
            Debug.log("IO ERROR", "Could not load file to start popup");
            e1.printStackTrace();
        }

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                parent.setDisable(false);
            }
        });
    }
    @FXML
    /**
     * Handles the cancel button. Closes the window.
     * @param event the button's event
     */
    private void handleDropButton(ActionEvent event)
    {
        if (dropAssignmentSelect.getValue() != null) {
            GradedItem dropS = null;
            for(GradedItem assignment: Grader.getRoster().getAssignments()){
                if(assignment.name() == dropAssignmentSelect.getValue()){
                    dropS = assignment;
                }
            }
                    
            Grader.getRoster().dropAssignment(dropS);
        }
        resetDropdown();
        gbook.fullRefresh();
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
     * Resets the parent dropdown menu to reflect any new assignments
     */
    private void resetDropdown() {
        /*GradedItem curChoice = Grader.getRoster().getAssignment(
                parentDropdown.getValue());*/
        String curChoice = dropAssignmentSelect.getValue();
        dropAssignmentSelect.setItems(Grader.getAssignmentNameList());
        dropAssignmentSelect.getItems().add(0, noParent);
        dropAssignmentSelect.setVisibleRowCount(numParentsShown);
        dropAssignmentSelect.setValue(curChoice);
    }
}
