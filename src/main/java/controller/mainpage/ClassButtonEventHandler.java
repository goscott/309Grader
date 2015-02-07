package controller.mainpage;


import model.driver.Debug;
import model.driver.Grader;
import model.roster.Roster;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
/**
 * 
 * handles the event of a class button being pressed
 * @author Michael Lenz
 *
 */
public class ClassButtonEventHandler implements EventHandler<ActionEvent>
{


    /**
     * handdles the event of a class button being pressed by attempting to  load the roster.
     */
    @Override
    public void handle(ActionEvent event)
    {
        Debug.log("class button clicked " + ((Button)event.getSource()).getText());
            
       Roster rost = Roster.load("Rosters/" + ((Button)event.getSource()).getText() + ".rost");
       if(rost != null)
       {
           Grader.setCurrentRoster(rost);
       }
       
    }
    
    
    
}
