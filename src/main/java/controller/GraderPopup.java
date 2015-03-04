package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.driver.Debug;
import resources.ResourceLoader;

/**
 * Standardizes Popup functionality
 * 
 * @author Gavin Scott
 */
public class GraderPopup {

	/**
	 * Sets the icon for a stage to the Grader logo
	 */
	public static void setIcon(Stage stage) {
		try {
			stage.getIcons().add(ResourceLoader.ICON);
		} catch (Exception ex) {
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
				toDisable.setDisable(false);
				Stage stage = GraderPopup.getPopupStage(toDisable.getText(),
						"view/roster/" + fxml + ".fxml");
				stage.setOnHiding(new EventHandler<WindowEvent>() {
					public void handle(WindowEvent event) {
						toDisable.setDisable(true);
					}
				});
				stage.show();
			}
		};
	}

	/**
	 * Makes and returns a stage, ensuring consistancy. Defaults to
	 * non-resizable, and a title of "Grader"
	 */
	public static Stage getPopupStage(String fxml) {
		return getPopupStage("Grader", fxml);
	}

	/**
	 * Makes and returns a stage, ensuring consistancy. Defaults to
	 * non-resizable.
	 */
	public static Stage getPopupStage(String title, String fxml) {
		Stage stage = new Stage();
		stage.setScene(new Scene(getResource(fxml)));
		stage.setTitle(title);
		stage.setResizable(false);
		setIcon(stage);
		return stage;
	}

	/**
	 * Loads an fxml resource
	 */
	public static Parent getResource(String fxml) {
		try {
			return FXMLLoader.load(GraderPopup.class.getClassLoader()
					.getResource(fxml));
		} catch (IOException ex) {
			ex.printStackTrace();
			Debug.log("ERROR", "Could not load " + fxml);
		}
		return null;
	}
}
