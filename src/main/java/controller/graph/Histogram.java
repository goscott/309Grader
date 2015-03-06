package controller.graph;

import resources.ResourceLoader;
import model.driver.Grader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Histogram extends Application {
	public static final int INCR_PER_PERSON = 25;
	public static final int DIST_TO_LINE = 25;
	public static final int BAR_WIDTH = 7;
	public static final int BUFFER = 1;
	public static final int SQUARE_START = 150;
	public static final int NUM_TICKS = 100;
	public static final int TICKS_UNTIL_TEXT = 10;
	public static final int TICK_WIDTH = 10;
	public static final int BIG_TICK_WIDTH = 20;
	public static final int MAIN_LINE_WIDTH = 3;

    @Override
    public void start(Stage stage) throws Exception {
        Pane drawingPane = new Pane();
        drawingPane.setPrefSize(800, 800);
        drawingPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ScrollPane scrollPane = new ScrollPane(drawingPane);
        scrollPane.setPrefSize(300, 500);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setBackground(new Background(new BackgroundFill(ResourceLoader.BACKGROUND, null, null)));
        drawingPane.setBackground(new Background(new BackgroundFill(ResourceLoader.BACKGROUND, null, null)));
        // screen pane size
        drawingPane.setMinHeight(NUM_TICKS * BAR_WIDTH);
        // y axis
        Rectangle axis = new Rectangle(DIST_TO_LINE - 1, 0, MAIN_LINE_WIDTH + 1, NUM_TICKS * BAR_WIDTH);
        axis.setFill(ResourceLoader.LINE_COLOR);
        drawingPane.getChildren().add(axis);
        
        for(int i = NUM_TICKS; i > 0; i--) {
        	if(i % TICKS_UNTIL_TEXT == 0) {
        		// Label and big tick
        		drawingPane.getChildren().add(new Text(DIST_TO_LINE / 4, (NUM_TICKS-i)*BAR_WIDTH, i + ""));
        		Line line = new Line(DIST_TO_LINE, (NUM_TICKS-i)*BAR_WIDTH, DIST_TO_LINE + BIG_TICK_WIDTH, (NUM_TICKS-i)*BAR_WIDTH);
        		line.setFill(ResourceLoader.LINE_COLOR);
        		drawingPane.getChildren().add(line);
        	} else {
        		// tick
        		Line line = new Line(DIST_TO_LINE, (NUM_TICKS-i)*BAR_WIDTH, DIST_TO_LINE + TICK_WIDTH, (NUM_TICKS-i)*BAR_WIDTH);
        		line.setFill(ResourceLoader.LINE_COLOR);
        		drawingPane.getChildren().add(line);
        	}
        	// bar for number of students
        	Rectangle rect = new Rectangle(DIST_TO_LINE+MAIN_LINE_WIDTH, (NUM_TICKS-i)*BAR_WIDTH+1, INCR_PER_PERSON*getNum(i), BAR_WIDTH - 2*BUFFER);
        	rect.setFill(ResourceLoader.BAR_COLOR);
        	drawingPane.getChildren().add(rect);
        }
        // add sliders
        drawingPane.getChildren().addAll(new GradeShapeGroup().get());
        
        Scene scene = new Scene(scrollPane);
        stage.setMinWidth(100);
        stage.setMinHeight(100);
        stage.setScene(scene);
        stage.show();
    }

	private int getNum(double score) {
		return Grader.getRoster().getNumStudentsWithScore(score);
	}
}