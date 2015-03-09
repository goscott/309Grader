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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GradeShape extends Rectangle {
	private Line line;
	private Text text;
	private Grade grade;
	private final double WIDTH = 30;
	private final double HEIGHT = 30;
	private final double ROUNDNESS = 20;
	private final double FONT_SCALE = 1.5;
	private final double CLOSENESS_THRESHOLD = 3;
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
		setX(Histogram.SQUARE_START);
		setY(y);
		text = new Text(grade.getName());
		text.setFill(Color.BLACK);
		text.setScaleX(FONT_SCALE);
		text.setScaleY(FONT_SCALE);
		text.setTextAlignment(TextAlignment.CENTER);

		// drag
		addEventFilter(MouseEvent.MOUSE_DRAGGED, getDragHandler());
		text.addEventFilter(MouseEvent.MOUSE_DRAGGED, getDragHandler());

		// release
		addEventFilter(MouseEvent.MOUSE_RELEASED, getReleaseHandler());
		text.addEventFilter(MouseEvent.MOUSE_RELEASED, getReleaseHandler());

		ContextMenu menu = new ContextMenu();
		MenuItem delete = new MenuItem("Remove Grade");
		menu.getItems().add(delete);
		menu.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Action response = Alert.showWarningDialog(
						"Warning: Grade deletion will be permanent",
						"Are you sure you want to delete '" + grade.getName()
								+ "' from the curve?");
				if (response == Dialog.ACTION_YES) {
					Grader.getCurve().remove(grade);
					Histogram.refresh();
				}
			}
		});

		addEventFilter(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						if (event.getButton() == MouseButton.SECONDARY) {
							menu.show(text, event.getScreenX(),
									event.getScreenY());
						}
					}
				});

		text.addEventFilter(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						if (event.getButton() == MouseButton.SECONDARY) {
							menu.show(text, event.getScreenX(),
									event.getScreenY());
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
	
	public Collection<Node> get() {
		ArrayList<Node> list = new ArrayList<Node>();
		list.add(this);
		list.add(line);
		//list.add(text);
		return list;
	}

	private void move(double y) {
		if (y % Histogram.BAR_WIDTH == 0 && moveValid(y + getHeight() / 2)) {
			setY(y - getHeight());
			setX(closeToOtherSlider() ? EXPANED_SLIDER_X
					: Histogram.SQUARE_START);
			moveLineAndText();
			Grader.getCurve().adjust(grade, getScoreFromLocation());
		}
	}

	private void moveLineAndText() {
		line.setStartX(getX());
		line.setStartY(getY() + getHeight() / 2);
		line.setEndX(Histogram.DIST_TO_LINE + Histogram.MAIN_LINE_WIDTH);
		line.setEndY(getY() + getHeight() / 2);
		text.setX(getX() + getWidth() / 2.5);
		text.setY(getY() + getHeight() / 1.7);
	}

	private boolean closeToOtherSlider() {
		return getScoreFromLocation() < getLowestPossibleScore()
				+ CLOSENESS_THRESHOLD
				|| getScoreFromLocation() > getHighestPossibleScore()
						- CLOSENESS_THRESHOLD;
	}

	private boolean moveValid(double y) {
		return line.getStartY() > Histogram.TOP_BUFFER
				&& line.getStartY() < Histogram.TOP_BUFFER
						+ Histogram.NUM_TICKS * Histogram.BAR_WIDTH;
//				&& getScoreFromLocation() > getLowestPossibleScore()
//				&& getScoreFromLocation() < getHighestPossibleScore();
	}

	private double getLowestPossibleScore() {
		if (Grader.getCurve().getGradeBelow(grade) != null) {
			return Grader.getCurve().getGradeBelow(grade).value() + 1;
		}
		return 0;
	}

	private double getHighestPossibleScore() {
		if (Grader.getCurve().getGradeAbove(grade) != null) {
			return Grader.getCurve().getGradeAbove(grade).value() - 1;
		}
		return 100;
	}

	private double getScoreFromLocation() {
		return ((line.getStartY() + Histogram.BAR_WIDTH / 2 - Histogram.TOP_BUFFER)
						/ Histogram.BAR_WIDTH - Histogram.NUM_TICKS) * -1;
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof GradeShape)) {
			return false;
		}
		return ((GradeShape) other).grade.equals(grade);
	}
}
