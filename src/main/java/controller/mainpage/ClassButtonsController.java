package controller.mainpage;

import java.io.File;
import java.io.FilenameFilter;

import model.driver.Debug;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class ClassButtonsController
{
    private FlowPane buttonSetUp;
    public ClassButtonsController(FlowPane buttons)
    {
        buttonSetUp = buttons;
        buttonSetUp.setTranslateX(25);
        buttonSetUp.setTranslateY(25);
        buttonSetUp.setVgap(50);
        buttonSetUp.setHgap(50);
        refreshButtons();
    }
    
    public void refreshButtons()
    {
        buttonSetUp.getChildren().clear();
        File dir = new File("Rosters");
        File[] rosters = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".rost");
            }
            
        });
        Button button;
        
        
        for(int i = 0; i < rosters.length; i++)
        {
            String name = rosters[i].getName();
            name = name.substring(0, name.length() - 5);
            button = new Button (name);
            button.setOnAction(new ClassButtonEventHandler());
            buttonSetUp.getChildren().add(button);
        }  
        
        button = new Button("Add Class");
        button.setOnAction(new AddClassButtonEventHandler(this));
        buttonSetUp.getChildren().add(button);
        
    }

}