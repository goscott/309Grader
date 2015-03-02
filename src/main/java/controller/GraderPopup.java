package controller;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.driver.Debug;
import run.Launcher;

public class GraderPopup {
	
	/**
	 * Sets the icon for a stage to the Grader logo
	 */
	public static void setIcon(Stage stage) {
		try {
        	stage.getIcons().add(new Image(Launcher.class.getResourceAsStream( "GraderIcon.png" )));
        }
        catch(Exception ex) {
        	Debug.log("Error", "Could not load icon");
        }
	}
	
}
