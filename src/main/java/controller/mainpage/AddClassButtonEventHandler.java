package controller.mainpage;

import controller.roster.AddClassDialogController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class AddClassButtonEventHandler implements EventHandler<ActionEvent>
{

    @Override
    public void handle(ActionEvent event)
    {
       new AddClassDialogController().start(new Stage());

    }

}
