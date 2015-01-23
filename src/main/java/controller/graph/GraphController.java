package controller.graph;

import model.curve.Grade;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controls the Graphs tab visuals and interface
 * @author Frank Poole
 */
public class GraphController {
    @FXML
    private Label gradeLabel;
    @FXML
    private Label percentageLabel;
    @FXML
    private TableView<Grade> curveTable;
    @FXML
    private TableColumn<Grade, String> gradeCol;
    
    public void initialize()
    {
        gradeLabel.setText("A+");
        percentageLabel.setText("100");
        
        gradeCol.setCellValueFactory(new PropertyValueFactory<Grade, String>(
                "name"));
    }
}
