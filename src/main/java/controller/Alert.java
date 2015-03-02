package controller;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import model.driver.Debug;
import model.driver.Grader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
	 * Shows a messasge with the given title and message.
	 * Messages have a 50 character limit.
	 */
	public static void show(String message) {
		if(message.length() > maxLength) {
			Debug.log("ERROR", "Alert message too long");
			return;
		}
		
		Stage dialogStage = new Stage(StageStyle.UTILITY);
		dialogStage.setTitle("Grader");
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
	
	public static Action showConfirmDialog(String headerMessage, String message) {
		return Dialogs
				.create()
				.owner(new Stage(StageStyle.UNDECORATED))
				.title("Grader")
				.masthead(headerMessage)
				.message(message)
				.actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
				.showConfirm();
	}
	
	public static Action showConfirmDialog(String message) {
		return Dialogs
				.create()
				.owner(new Stage(StageStyle.UNDECORATED))
				.title("Grader")
				.message(message)
				.actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
				.showConfirm();
	}

	public static Action showWarningDialog(String message) {
		return Dialogs
				.create()
				.owner(new Stage(StageStyle.UNDECORATED))
				.title("Grader")
				.masthead("Warning: Cannot be undone")
				.message(message)
				.actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
				.showWarning();
	}
}
