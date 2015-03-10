package controller.graph;

import java.util.ArrayList;
import java.util.Collection;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

import controller.Alert;
import controller.roster.GradebookController;
import model.curve.Grade;
import model.driver.Grader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class GradeShape extends Rectangle {
	private Line line;
	private Grade grade;
	private ContextMenu menu;
	private final double WIDTH = 30;
	private final double HEIGHT = 30;
	private final double ROUNDNESS = 20;
	private final double CLOSENESS_THRESHOLD = 5;
	private final double EXPANED_SLIDER_X = Histogram.SQUARE_START + WIDTH
			* 1.5;

	public GradeShape(double y, Grade grade) {
		this.grade = grade;
		setArcHeight(ROUNDNESS);
		setArcWidth(ROUNDNESS);
		Color color = new Color(((double) grade.getColor().getRed()) / 255,
				((double) grade.getColor().getGreen()) / 255, ((double) grade
						.getColor().getBlue()) / 255, 1);
		setFill(color);
		setHeight(HEIGHT);
		setWidth(WIDTH);
		setY(y);
		setX(Histogram.SQUARE_START);

		addEventFilter(MouseEvent.MOUSE_DRAGGED, getDragHandler());
		addEventFilter(MouseEvent.MOUSE_RELEASED, getReleaseHandler());
		addEventFilter(MouseEvent.MOUSE_CLICKED, getClickHandler());
		
		menu = new ContextMenu();
		MenuItem delete = new MenuItem("Remove Grade");
		menu.getItems().add(delete);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Action response = Alert.showWarningQuestion(
						"Warning: Grade deletion will be permanent",
						"Are you sure you want to delete '" + grade.getName()
								+ "' from the curve?");
				if (response == Dialog.ACTION_YES) {
					Grader.getCurve().remove(grade);
					Histogram.refresh();
				}
			}
		});

		line = new Line();
		moveLineAndText();
	}

	private EventHandler<MouseEvent> getDragHandler() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				GraphController.get().setSelectedGrade(grade);
				move(event.getY());
			}
		};
	}
	
	private EventHandler<MouseEvent> getReleaseHandler() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				GradebookController.get().fullRefresh();
				GraphController.refresh();
			}
		};
	}
	
	private EventHandler<MouseEvent> getClickHandler() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY) {
					menu.show(line, event.getX(),
							event.getY());
				}
			}
		};
	}
	
	public Collection<Node> get() {
		ArrayList<Node> list = new ArrayList<Node>();
		list.add(this);
		list.add(line);
		//list.add(text);
		return list;
	}

	private void move(double y) {
		if (y % Histogram.BAR_WIDTH == 0 && moveValid(y - getHeight() / 2)) {
			setY(y - getHeight());
			setX(closeToOtherSlider() ? EXPANED_SLIDER_X
					: Histogram.SQUARE_START);
			moveLineAndText();
			Grader.getCurve().adjust(grade, getScoreFromLocation(getLineY()));
		}
	}

	private void moveLineAndText() {
		line.setStartX(getX());
		line.setStartY(getY() + getHeight() / 2);
		line.setEndX(Histogram.DIST_TO_LINE + Histogram.MAIN_LINE_WIDTH);
		line.setEndY(getY() + getHeight() / 2);
	}

	private boolean closeToOtherSlider() {
		return getScoreFromLocation(getLineY()) < getLowestPossibleScore()
				+ CLOSENESS_THRESHOLD
				|| getScoreFromLocation(getLineY()) > getHighestPossibleScore()
						- CLOSENESS_THRESHOLD;
	}

	private boolean moveValid(double y) {
		return y > Histogram.TOP_BUFFER
				&& y < Histogram.TOP_BUFFER
						+ Histogram.NUM_TICKS * Histogram.BAR_WIDTH
				&& getScoreFromLocation(y) > getLowestPossibleScore()
				&& getScoreFromLocation(y) < getHighestPossibleScore();
	}

	private double getLowestPossibleScore() {
		if (Grader.getCurve().getGradeBelow(grade) != null) {
			return Grader.getCurve().getGradeBelow(grade).value();
		}
		return 0;
	}

	private double getHighestPossibleScore() {
		if (Grader.getCurve().getGradeAbove(grade) != null) {
			return Grader.getCurve().getGradeAbove(grade).value();
		}
		return 100;
	}

	private double getScoreFromLocation(double y) {
		return 1 + ((y + Histogram.BAR_WIDTH / 2 - Histogram.TOP_BUFFER)
						/ Histogram.BAR_WIDTH - Histogram.NUM_TICKS) * -1;
	}

	private double getLineY() {
		return getY() + getHeight() / 2;
	}
	
	public boolean equals(Object other) {
		if (other == null || !(other instanceof GradeShape)) {
			return false;
		}
		return ((GradeShape) other).grade.equals(grade);
	}
}
