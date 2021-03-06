package controller;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Standardizes generic popups, alerts, and warnings, using controlsfx dialogs
 * 
 * @author Gavin Scott
 */
@SuppressWarnings("deprecation")
public class Alert {

	/**
	 * Shows a messasge with the given message
	 */
	public static void show(String message) {
		Dialogs.create().owner(new Stage(StageStyle.UNDECORATED))
				.title("Grader").masthead(null).message(message)
				.showInformation();
	}

	/**
	 * Shows a dialog with the given header message and message, returning
	 * Dialog.ACTION_YES or Dialog.ACTION_NO depending on what the user selects
	 */
	public static Action showConfirmDialog(String headerMessage, String message) {
		return Dialogs.create().owner(new Stage(StageStyle.UNDECORATED))
				.title("Grader").masthead(headerMessage).message(message)
				.actions(Dialog.ACTION_YES, Dialog.ACTION_NO).showConfirm();
	}

	/**
	 * Shows a dialog with the given message, returning Dialog.ACTION_YES or
	 * Dialog.ACTION_NO depending on what the user selects
	 */
	public static Action showConfirmDialog(String message) {
		return Dialogs.create().owner(new Stage(StageStyle.UNDECORATED))
				.title("Grader").message(message)
				.actions(Dialog.ACTION_YES, Dialog.ACTION_NO).showConfirm();
	}

	/**
	 * Shows an alert dialog with the given message, returning Dialog.ACTION_YES
	 * or Dialog.ACTION_NO depending on what the user selects
	 */
	public static Action showWarningQuestion(String errorMessage, String message) {
		return Dialogs.create().owner(new Stage(StageStyle.UNDECORATED))
				.title("Grader").masthead(errorMessage).message(message)
				.actions(Dialog.ACTION_YES, Dialog.ACTION_NO).showWarning();
	}
	
	/**
	 * Shows an alert message with the given message
	 */
	public static Action showWarningMessage(String errorMessage, String message) {
		return Dialogs.create().owner(new Stage(StageStyle.UNDECORATED))
				.title("Grader").masthead(errorMessage).message(message)
				.actions(Dialog.ACTION_OK).showWarning();
	}
}
