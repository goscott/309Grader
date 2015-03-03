package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
	
	/**
	 * Makes a handler for a popup menu item
	 */
	public static EventHandler<ActionEvent> getPopupHandler(String fxml,
			MenuItem toDisable) {
		return new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					Stage stage = new Stage();
					toDisable.setDisable(false);
					stage.setTitle(toDisable.getText());
					stage.setScene(new Scene(FXMLLoader.load(getClass()
							.getClassLoader().getResource(
									"view/roster/" + fxml + ".fxml"))));
					stage.setResizable(false);
					GraderPopup.setIcon(stage);
					stage.setOnHiding(new EventHandler<WindowEvent>() {
						public void handle(WindowEvent event) {
							toDisable.setDisable(true);
						}
					});
					stage.show();
				} catch (IOException ex) {
					Debug.log("IO ERROR", "Could not load file to start "
							+ fxml + ".fxml");
					ex.printStackTrace();
				}
			}
		};
	}
}
