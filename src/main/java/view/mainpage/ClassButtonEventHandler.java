package view.mainpage;


import model.driver.Debug;
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
        
    }
    
    
    
}
