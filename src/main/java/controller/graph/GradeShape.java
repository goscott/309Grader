package controller.graph;

import java.util.ArrayList;
import java.util.Collection;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

import controller.Alert;
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

/**
 * A draggable handle for manipulating the curve in the histogram
 * 
 * @author Gavin Scott
 */
public class GradeShape extends Rectangle {
	/** The line **/
	private Line line;
	/** The grade associated with the shape **/
	private Grade grade;
	/** The width of the rectangle **/
	private final double WIDTH = 30;
	/** The height of the rectangle **/
	private final double HEIGHT = 30;
	/** The beveled-ness of the rectgangle **/
	private final double ROUNDNESS = 20;
	/** How many points away a shape has to be from another to extend the line **/
	private final double CLOSENESS_THRESHOLD = 5;
	/** The long length of the line 8 **/
	private final double EXPANED_SLIDER_X = Histogram.SQUARE_START + WIDTH
			* 1.5;

	/**
	 * Creates a gradeshape
	 */
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
		addEventFilter(MouseEvent.MOUSE_PRESSED, getClickHandler());

		MenuItem delete = new MenuItem("Remove Grade");
		line = new Line();
		moveLine();
	}

	/**
	 * Creates an event handler to handle the mouse drag
	 */
	private EventHandler<MouseEvent> getDragHandler() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				GraphController.get().setSelectedGrade(grade);
				move(event.getY());
			}
		};
	}

	/**
	 * Creates an event handler to handle the mouse release
	 */
	private EventHandler<MouseEvent> getReleaseHandler() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				// GradebookController.get().fullRefresh();
				GraphController.refresh();
			}
		};
	}

	/**
	 * Creates an event handler to handle the mouse click
	 */
	private EventHandler<MouseEvent> getClickHandler() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY
						&& event.isControlDown()) {
					Action response = Alert.showWarningQuestion(
							"Warning: Grade deletion will be permanent",
							"Are you sure you want to delete '"
									+ grade.getName() + "' from the curve?");
					if (response == Dialog.ACTION_YES) {
						Grader.getCurve().remove(grade);
						Histogram.refresh();
					}
				}
			}
		};
	}

	/**
	 * Gets a collection of all the nodes that are a part of this grade shape
	 */
	public Collection<Node> get() {
		ArrayList<Node> list = new ArrayList<Node>();
		list.add(this);
		list.add(line);
		return list;
	}

	/**
	 * Moves the shape to the provided y coordinate
	 */
	private void move(double y) {
		if ((y + Histogram.BAR_WIDTH / 2) % Histogram.BAR_WIDTH == 0
				&& moveValid(y - getHeight() / 2)) {
			setY(y - getHeight());
			setX(closeToOtherSlider() ? EXPANED_SLIDER_X
					: Histogram.SQUARE_START);
			moveLine();
			Grader.getCurve().adjust(grade, getScoreFromLocation(getLineY()));
		}
	}

	/**
	 * Moves the line to the center of the gradebook
	 */
	private void moveLine() {
		line.setStartX(getX());
		line.setStartY(getY() + getHeight() / 2);
		line.setEndX(Histogram.DIST_TO_LINE + Histogram.MAIN_LINE_WIDTH);
		line.setEndY(getY() + getHeight() / 2);
	}

	/**
	 * Checks if this shape is close to another in the histogram
	 */
	private boolean closeToOtherSlider() {
		return getScoreFromLocation(getLineY()) < getLowestPossibleScore()
				+ CLOSENESS_THRESHOLD
				|| getScoreFromLocation(getLineY()) > getHighestPossibleScore()
						- CLOSENESS_THRESHOLD;
	}

	/**
	 * Checks if a move is valid. Gradeshapes cannot be moved past other shapes
	 * or off the histogram.
	 */
	private boolean moveValid(double y) {
		return y > Histogram.TOP_BUFFER
				&& y < Histogram.TOP_BUFFER + Histogram.NUM_TICKS
						* Histogram.BAR_WIDTH
				&& getScoreFromLocation(y) > getLowestPossibleScore()
				&& getScoreFromLocation(y) < getHighestPossibleScore();
	}

	/**
	 * Gets the lowest score this grade can be set to, based on the others in
	 * the curve
	 */
	private double getLowestPossibleScore() {
		if (Grader.getCurve().getGradeBelow(grade) != null) {
			return Grader.getCurve().getGradeBelow(grade).value();
		}
		return 0;
	}

	/**
	 * Gets the highest score this grade can be set to, based on the others in
	 * the curve
	 */
	private double getHighestPossibleScore() {
		if (Grader.getCurve().getGradeAbove(grade) != null) {
			return Grader.getCurve().getGradeAbove(grade).value();
		}
		return 100;
	}

	/**
	 * Gets the grade on the histogram based on the y coordinate
	 */
	private double getScoreFromLocation(double y) {
		return 1
				+ ((y + Histogram.BAR_WIDTH / 2 - Histogram.TOP_BUFFER)
						/ Histogram.BAR_WIDTH - Histogram.NUM_TICKS) * -1;
	}

	/**
	 * Gets the y coordinate of the line
	 */
	private double getLineY() {
		return getY() + getHeight() / 2;
	}

	/**
	 * Compares two gradeshapes for equality based on their grades
	 */
	public boolean equals(Object other) {
		if (other == null || !(other instanceof GradeShape)) {
			return false;
		}
		return ((GradeShape) other).grade.equals(grade);
	}
}
