package controller;

import java.io.IOException;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Robot;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
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
		if(!fxml.contains(".fxml")) {
			fxml += ".fxml";
		}
		try {
			return FXMLLoader.load(GraderPopup.class.getClassLoader()
					.getResource(fxml));
		} catch (IOException ex) {
			ex.printStackTrace();
			Debug.log("ERROR", "Could not load " + fxml);
		}
		return null;
	}
	
	/**
	 * Shows a tooltip
	 * @param message The message to be displayed
	 * @param node Any fx node (object) in the scene, so the
	 * tooltip can be set in the same stage (does not have to
	 * be tied to the tooltip at all)
	 */
	@SuppressWarnings("restriction")
	public static void showTooltip(String message, Node node) {
		Robot robot = Application.GetApplication().createRobot();
        Tooltip tt = new Tooltip(message);
        tt.setX(robot.getMouseX()+10);
        tt.setY(robot.getMouseY()-10);
        tt.setAutoHide(true);
        tt.show((Stage) node.getScene().getWindow());
	}
}
