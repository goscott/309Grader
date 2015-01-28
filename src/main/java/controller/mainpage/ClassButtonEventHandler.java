package controller.mainpage;


import model.driver.Debug;
import model.driver.Grader;
import model.roster.Roster;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ClassButtonEventHandler implements EventHandler<ActionEvent>
{



    @Override
    public void handle(ActionEvent event)
    {
        Debug.log("class button clicked " + ((Button)event.getSource()).getText());
            
       Roster rost = Roster.load(((Button)event.getSource()).getText() + ".rost");
       if(rost != null)
       {
           Grader.setCurrentRoster(rost);
       }
       
    }
    
    
    
}
