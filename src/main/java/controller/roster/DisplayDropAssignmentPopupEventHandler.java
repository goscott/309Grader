package controller.roster;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * An ActionEvent that handles displaying the
 * "Drop Assignment" dialog.
 * @author Gavin Scott
 * @author Shelli Crispen 
 *
 */
@SuppressWarnings("serial")
public class DisplayDropAssignmentPopupEventHandler extends ActionEvent implements EventHandler<ActionEvent> {
    /** The gradebook controller **/
    private GradebookController contr;
    /** The MenuItem that displayed the window **/
    private MenuItem callingItem;
    
    /**
     * Creates an event handler with the given parameters
     * @param callingItem The MenuItem that triggered the event
     * @param contr The GradebookController that the dialog will edit
     */
    public DisplayDropAssignmentPopupEventHandler(MenuItem callingItem, GradebookController contr) {
        this.contr = contr;
        this.callingItem = callingItem;
    }
    
    /**
     * Handles the event
     * @param event The ActionEvent
     */
    public void handle(ActionEvent event) {
        Stage newStage = new Stage();
        DropAssignmentDialogController popup = new DropAssignmentDialogController();
        popup.setParent(callingItem, contr);
        popup.start(newStage);
    }
}