package controller.mainpage;

import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class ClassButtonsController
{

    public ClassButtonsController(FlowPane buttonSetUp)
    {
        
        buttonSetUp.setTranslateX(25);
        buttonSetUp.setTranslateY(25);
        buttonSetUp.setVgap(50);
        buttonSetUp.setHgap(50);
        Button button = new Button("309");
        button.setOnAction(new ClassButtonEventHandler());
        buttonSetUp.getChildren().add(button);
        button = new Button("308");
        button.setOnAction(new ClassButtonEventHandler());
        buttonSetUp.getChildren().add(button);
        //TODO make buttons mean something and build based on files found.
        button = new Button("Add Class");
        button.setOnAction(new AddClassButtonEventHandler());
        buttonSetUp.getChildren().add(button);
    }

}
