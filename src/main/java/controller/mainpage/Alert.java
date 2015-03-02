package controller.mainpage;

import controller.GraderPopup;
import model.driver.Debug;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A generic informational popup to replace
 * JOptionPane, which does not work well with
 * javaFX.
 * 
 * @author Gavin Scott
 */
@SuppressWarnings("deprecation")
public class Alert {
	/** The max length of a message **/
	private static int maxLength = 50;
	
	/**
	 * Shows a messasge with the given message.
	 * Messages have a 50 character limit.
	 */
	public static void show(String message) {
		show("Alert", message);
	}
	
	/**
	 * Shows a messasge with the given title and message.
	 * Messages have a 50 character limit.
	 */
	public static void show(String title, String message) {
		if(message.length() > maxLength) {
			Debug.log("ERROR", "Alert message too long");
			return;
		}
		
		Stage dialogStage = new Stage();
		dialogStage.setTitle(title);
		dialogStage.setHeight(150);
		dialogStage.setWidth(350);
		dialogStage.setResizable(false);
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.setScene(new Scene(VBoxBuilder.create().
		    children(new Text(message)).
		    alignment(Pos.CENTER).padding(new Insets(5)).build()));
		GraderPopup.setIcon(dialogStage);
		dialogStage.show();
	}
}
