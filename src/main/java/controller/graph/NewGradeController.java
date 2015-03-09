package controller.graph;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Stage;

import java.awt.Color;

import controller.roster.GradebookController;
import resources.ResourceLoader;
import model.curve.Grade;
import model.driver.Grader;

/**
 * The controller for the "Add New Grade" pop up dialog
 * @author Gavin Scott
 */
public class NewGradeController {
	@FXML
	/** Grade color picker */
	private ColorPicker colorPicker;
	@FXML
	/** Enter grade text field */
	private TextField enterGrade;
	@FXML
	/** Enter percentage text field */
	private TextField enterPercent;
	@FXML
	/** Complete button */
	private Button doneButton;

	/**
	 * Initializes listeners on the pop up dialog.
	 */
	public void initialize() {
		doneButton.setDisable(true);
		enterGrade.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				doneButton.setDisable(isInvalid());
				if(isGradeInvalid()) {
					enterGrade.setBackground(new Background(new BackgroundFill(
							ResourceLoader.ERROR_RED, CornerRadii.EMPTY, Insets.EMPTY)));
				} else {
					enterGrade.setBackground(new Background(new BackgroundFill(
							ResourceLoader.NOERROR_WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				}
			}
		});
		enterPercent.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				doneButton.setDisable(isInvalid());
				if(isPercentInvalid()) {
					enterPercent.setBackground(new Background(new BackgroundFill(
							ResourceLoader.ERROR_RED, CornerRadii.EMPTY, Insets.EMPTY)));
				} else {
					enterPercent.setBackground(new Background(new BackgroundFill(
							ResourceLoader.NOERROR_WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				}
			}
		});
	}

	/**
	 * Determines if the provided information is valid for a new grade
	 */
	@FXML
	private boolean isInvalid() {
		if (enterGrade.getText().length() == 0
				|| enterPercent.getText().length() == 0) {
			return true;
		} else {
			return isPercentInvalid() || isGradeInvalid();
		}
	}

	/**
	 * Return true if this is a valid percentage.
	 * @return true if this is a valid percentage
	 */
	private boolean isPercentInvalid() {
		try {
			double val = Double.parseDouble(enterPercent.getText());
			if (val < 0 || val > 100) {
				return true;
			}
			for (Grade grade : Grader.getCurve().getGrades()) {
				if (grade.value() == val) {
					return true;
				}
			}
		} catch (Exception ex) {
			return true;
		}
		return false;
	}

	/**
	 * Return true if this is an invalid grade.
	 * @return true if this is an invalid grade
	 */
	private boolean isGradeInvalid() {
		for (Grade grade : Grader.getCurve().getGrades()) {
			if (grade.getName().equals(enterGrade.getText())) {
				return true;
			}
		}
		return false;
	}

	@FXML
	/**
	 * Cancel the action event.
	 * @param event the action event
	 */
	private void cancel(ActionEvent event) {
		((Stage) enterGrade.getScene().getWindow()).close();
	}

	@FXML
	/**
	 * Complete the action event.
	 * @param event the action event
	 */
	private void done(ActionEvent event) {		
		Color color = new Color((int)(colorPicker.getValue().getRed()*255),
				(int) (colorPicker.getValue().getGreen()*255), (int) (colorPicker
						.getValue().getBlue()*255));
		Grade grade = new Grade(enterGrade.getText(),
				Double.parseDouble(enterPercent.getText()), color);
		Grader.getCurve().add(grade);
		enterGrade.setText("");
		enterPercent.setText("");
		colorPicker.setValue(javafx.scene.paint.Color.WHITE);
		GraphController.refresh();
		GradebookController.edited = true;
	}
}