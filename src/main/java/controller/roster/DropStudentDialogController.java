package controller.roster;

import java.io.IOException;

import controller.GraderPopup;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Student;
import model.server.Server;

/**
 * controls the Drop Student dialog functionality incomplete
 * @author Shelli Crispen
 * 
 */
public class DropStudentDialogController
{
    /** contains the ComboBox with all the students names to add **/
    @FXML
    private ComboBox<String> SelectAStudent;
    /** the add button  **/
    @FXML
    private Button dropButton;
    /** the cancel button  **/
    @FXML
    private Button cancelButton;
    /** The controller for the gradebook **/
    private static GradebookController gbook;
    
    /**
     * Initializes the dropdown box. 
     */
    public void initialize() {
    	gbook = GradebookController.get();
        resetDropdown();
    }
    
    /**
     * Creates the popup on the given stage
     * 
     * @param stage the stage
     */
    public void start(Stage stage) {
        try {
            AnchorPane page = (AnchorPane) FXMLLoader.load(getClass().getClassLoader()
                    .getResource("view/roster/DropStudent.fxml"));
            Scene popup = new Scene(page);
            stage.setTitle("Drop Student");
            stage.setScene(popup);
            stage.setResizable(false);
            GraderPopup.setIcon(stage);
            stage.show();
        } catch (IOException ex) {
            Debug.log("IO ERROR", "Could not load file to start popup");
            ex.printStackTrace();
        }
    }
    
    @FXML
    /**
     * Handles the Add button. Closes the window.
     * @param event the button's event
     */
    private void handleDropButton(ActionEvent event){
        if (SelectAStudent.getValue() != null) {
            Student dropS = null;
            for(Student student: Server.getStudentListInRoster()){
                if(student.getName() == SelectAStudent.getValue()){
                    dropS = student;
                }
            }
                    
            Grader.getRoster().dropStudent(dropS);
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
     * Resets the parent dropdown menu to reflect
     * that students are added to the gradebook and no longer only in the server
     */
    private void resetDropdown() {
        ObservableList<String> studentList = Server.getStudentListNameInRoster();
        if(studentList != null){
            SelectAStudent.setItems(studentList);
        }
    }
   
}
