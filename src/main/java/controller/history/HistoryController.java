package controller.history;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Controlls the history view.
 * @author Mason Stevenson
 *
 */
public class HistoryController {
    
    /**
     * Shows course history info.
     */
    @FXML
    private HBox history_view;
    
    /**
     * Shows completed (non-editable) gradebook for this course.
     */
    @FXML 
    private BorderPane gradebook_view;
    
    /**
     * Switches to gradebook view.
     */
    @FXML
    private Button button_gradebook;
    
    /**
     * Switches to history view.
     */
    @FXML
    private Button button_history;
    
    public void initialize() {
        //load classes from history db
    }
    
    /**
     * Switches the view from history to gradebook.
     */
    public void switchToGradebook() {
        history_view.setVisible(false);
        gradebook_view.setVisible(true);
        
        try {
            TableView<?> gradebookPage = (TableView<?>) FXMLLoader
                    .load(getClass().getClassLoader().getResource(
                            "view/roster/gradebook_screen.fxml"));
            gradebook_view.setCenter(gradebookPage);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Switches the view from gradebook to history.
     */
    public void switchToHistory() {
        history_view.setVisible(true);
        gradebook_view.setVisible(false);
    }
}
