package controller.history;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    
    @FXML
    private Accordion class_selector;
    
    @FXML
    private BorderPane intro_view;
    
    @FXML
    private BorderPane course_view;
    
    @FXML
    private BorderPane section_view;
    
    private int num = 1;
    
    public void initialize() {
        //load classes from history db
    }
    
    public void addClass() {
        class_selector.getPanes().add(new TitledPane("Dynamic Pane #" + num++, null));
    }
    
    public void switchToIntro() {
        course_view.setVisible(false);
        intro_view.setVisible(true);
        section_view.setVisible(false);
    }
    
    public void switchToCourse() {
        course_view.setVisible(true);
        intro_view.setVisible(false);
        section_view.setVisible(false);
    }
    
    public void switchToSection() {
        course_view.setVisible(false);
        intro_view.setVisible(false);
        section_view.setVisible(true);
    }
    
    /**
     * Switches the view from history to gradebook.
     */
    public void switchToGradebook() {
        history_view.setVisible(false);
        gradebook_view.setVisible(true);
        section_view.setVisible(false);
        
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
