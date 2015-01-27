package controller.history;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class HistoryController {
    @FXML
    private HBox history_view;
    
    @FXML 
    private BorderPane gradebook_view;
    
    @FXML
    private Button button_gradebook;
    
    @FXML
    private Button button_history;
    
    public void switchToGradebook() {
        history_view.setVisible(false);
        gradebook_view.setVisible(true);
    }
    
    public void switchToHistory() {
        history_view.setVisible(true);
        gradebook_view.setVisible(false);
    }
}
