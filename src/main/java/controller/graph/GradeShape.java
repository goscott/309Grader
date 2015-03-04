package controller.graph;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;

import model.curve.Grade;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GradeShape extends Rectangle {
	private Line line;
	private Text text;
	private final double WIDTH = 30;
	private final double HEIGHT = 30;
	private final double ROUNDNESS = 20;
	private final double FONT_SCALE = 1.5;

	public GradeShape(double x, double y, Grade grade) {
		setArcHeight(ROUNDNESS);
		setArcWidth(ROUNDNESS);
		setFill(new Color(grade.getColor().getRed() / 255, grade.getColor()
				.getGreen() / 255, grade.getColor().getBlue() / 255, 1));
		setHeight(HEIGHT);
		setWidth(WIDTH);
		setX(x);
		setY(y);
		text = new Text(grade.getName());
		text.setFill(Color.BLACK);
		text.setScaleX(FONT_SCALE);
		text.setScaleY(FONT_SCALE);
		text.setTextAlignment(TextAlignment.CENTER);
		
		text.addEventFilter(MouseEvent.MOUSE_DRAGGED,
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				move(event.getSceneY());
			}
		});
		
		addEventFilter(MouseEvent.MOUSE_DRAGGED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						move(event.getSceneY());
					}
				});

		line = new Line();
		moveLineAndText();
	}

	public Line getLine() {
		return line;
	}

	public Collection<Node> get() {
		ArrayList<Node> list = new ArrayList<Node>();
		list.add(this);
		list.add(line);
		list.add(text);
		return list;
	}

	private void move(double y) {
		setY(y - getHeight() / 2);
		moveLineAndText();
	}

	private void moveLineAndText() {
		line.setStartX(getX());
		line.setStartY(getY() + getHeight() / 2);
		line.setEndX(0);
		line.setEndY(getY() + getHeight() / 2);

		text.setX(getX() + getWidth() / 2.5);
		text.setY(getY() + getHeight() / 1.7);
	}
}
