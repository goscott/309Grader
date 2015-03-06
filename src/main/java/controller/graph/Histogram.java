package controller.graph;

import resources.ResourceLoader;
import model.driver.Grader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Histogram extends Application {
	public static final int INCR_PER_PERSON = 35;
	public static final int DIST_TO_LINE = 30;
	public static final int BAR_WIDTH = 6;
	public static final int BUFFER = 1;
	public static final int SQUARE_START = 150;
	public static final int NUM_TICKS = 100;
	public static final int TICKS_UNTIL_TEXT = 10;
	public static final int TICK_WIDTH = 10;
	public static final int BIG_TICK_WIDTH = 20;
	public static final int MAIN_LINE_WIDTH = 3;
	public static final int MIN_LINES = 15;
	public static final int TOP_BUFFER = 30;
	
	private static final int DEFAULT_PANE_WIDTH = 300;
	private static final int DEFAULT_PANE_HEIGHT = 500;
	
	private int maxNumber = 0;
	private static ScrollPane scrollPane;
	private static Pane drawingPane;
	
	private static Histogram singleton;

	public void initialize() {
		singleton = this;
		drawingPane = new Pane();
        drawingPane.setPrefSize(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT);
        drawingPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane = new ScrollPane(drawingPane);
        scrollPane.setPrefSize(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setBackground(new Background(new BackgroundFill(ResourceLoader.BACKGROUND, null, null)));
        drawingPane.setBackground(new Background(new BackgroundFill(ResourceLoader.BACKGROUND, null, null)));
        // screen pane size
        drawingPane.setMinHeight(NUM_TICKS * BAR_WIDTH + 2*TOP_BUFFER);
        update();
	}
	
	public void update() {
		drawingPane.getChildren().removeAll(drawingPane.getChildren());
		for(int i = NUM_TICKS; i > 0; i--) {
        	if(i % TICKS_UNTIL_TEXT == 0) {
        		// Label and big tick
        		drawingPane.getChildren().add(new Text(DIST_TO_LINE / 4, (NUM_TICKS-i)*BAR_WIDTH+TOP_BUFFER+BAR_WIDTH, i + ""));
        		Line line = new Line(DIST_TO_LINE, (NUM_TICKS-i)*BAR_WIDTH + TOP_BUFFER, DIST_TO_LINE + BIG_TICK_WIDTH, (NUM_TICKS-i)*BAR_WIDTH+TOP_BUFFER);
        		line.setFill(ResourceLoader.LINE_COLOR);
        		drawingPane.getChildren().add(line);
        	} else {
        		// tick
        		Line line = new Line(DIST_TO_LINE, (NUM_TICKS-i)*BAR_WIDTH+TOP_BUFFER, DIST_TO_LINE + TICK_WIDTH, (NUM_TICKS-i)*BAR_WIDTH+TOP_BUFFER);
        		line.setFill(ResourceLoader.LINE_COLOR);
        		drawingPane.getChildren().add(line);
        	}
        	// bar for number of students
        	Rectangle rect = new Rectangle(DIST_TO_LINE+MAIN_LINE_WIDTH, (NUM_TICKS-i)*BAR_WIDTH+1+TOP_BUFFER, INCR_PER_PERSON*getNum(i), BAR_WIDTH - 2*BUFFER);
        	rect.setFill(ResourceLoader.BAR_COLOR);
        	drawingPane.getChildren().add(rect);
		}
    	// student number lines
        for(int count = 1; count < ((maxNumber + 1) > MIN_LINES ? (maxNumber + 1) : MIN_LINES); count++) {
        	Line line = new Line(DIST_TO_LINE + MAIN_LINE_WIDTH + count*INCR_PER_PERSON, TOP_BUFFER, DIST_TO_LINE + MAIN_LINE_WIDTH + count*INCR_PER_PERSON, TOP_BUFFER+BAR_WIDTH*NUM_TICKS);
        	line.getStrokeDashArray().addAll(3d);
        	drawingPane.getChildren().add(line);
        }
        
        // add sliders
        drawingPane.getChildren().addAll(new GradeShapeGroup().get());
        
        // x and y axis
        Rectangle y_axis = new Rectangle(DIST_TO_LINE - 1, TOP_BUFFER, MAIN_LINE_WIDTH + 1, NUM_TICKS * BAR_WIDTH);
        y_axis.setFill(ResourceLoader.LINE_COLOR);
        Rectangle x_axis = new Rectangle(DIST_TO_LINE - 1, TOP_BUFFER + BAR_WIDTH*NUM_TICKS, MIN_LINES*INCR_PER_PERSON, MAIN_LINE_WIDTH);
        x_axis.setFill(ResourceLoader.LINE_COLOR);
        drawingPane.getChildren().addAll(y_axis, x_axis);
        
	}
	
	public static void refresh() {
		singleton.update();
	}
	
    @Override
    public void start(Stage stage) throws Exception {
    	initialize();
        Scene scene = new Scene(drawingPane);
        stage.setMinWidth(100);
        stage.setMinHeight(100);
        stage.setScene(scene);
        stage.show();
    }

	private int getNum(double score) {
		int num = Grader.getRoster().getNumStudentsWithScore(score);
		if(num > maxNumber) {
			maxNumber = num;
		}
		if(drawingPane.getWidth() < maxNumber*INCR_PER_PERSON + 3*INCR_PER_PERSON) {
			drawingPane.setMinWidth(maxNumber*INCR_PER_PERSON + 3*INCR_PER_PERSON);
		}
		return num;
	}
	
	public static double getScrollLevel() {
		return scrollPane.getVvalue();
	}
}