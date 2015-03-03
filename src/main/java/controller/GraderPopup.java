package controller;

import javafx.stage.Stage;
import model.driver.Debug;
import resources.ResourceLoader;

public class GraderPopup {
	
	/**
	 * Sets the icon for a stage to the Grader logo
	 */
	public static void setIcon(Stage stage) {
		try {
        	stage.getIcons().add(ResourceLoader.ICON);
        }
        catch(Exception ex) {
        	Debug.log("Error", "Could not load icon");
        }
	}
	
}
