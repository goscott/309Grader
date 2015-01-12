package roster;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class GradebookTable {
    @FXML
    private TableView<?> mainTable;
    @FXML
    private TableColumn<?, ?> idCol;
    @FXML
    private TableColumn<?, ?> totalGradeCol;
    @FXML
    private TableColumn<?, ?> nameCol;
}

