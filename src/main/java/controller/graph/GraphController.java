package controller.graph;

import model.curve.Grade;
import model.driver.Grader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controls the Graphs tab visuals and interface
 * @author Frank Poole
 */
public class GraphController {
    @FXML
    private Label studentsLabel;
    @FXML
    private Label gradeLabel;
    @FXML
    private Label percentageLabel;
    @FXML
    private TableView<Grade> curveTable;
    @FXML
    private TableColumn<Grade, String> gradeCol;
    
    /**
     * Initialize the graph tab view.
     */
    public void initialize()
    {
        this.update();
    }
    
    /**
     * Update the graph tab view with class roster information.
     */
    public void update() {
        studentsLabel.setText(Integer.toString(Grader.getRoster().numStudents()));
        gradeLabel.setText(Grader.getRoster().getLetterAverage());
        percentageLabel.setText(Double.toString(Grader.getRoster().getPercentAverage()));
    }
}
