package controller.graph;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class TEST2 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane drawingPane = new Pane();
        drawingPane.setPrefSize(800, 800);
        drawingPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ScrollPane scrollPane = new ScrollPane(drawingPane);
        scrollPane.setPrefSize(300, 300);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-focus-color: transparent;");

        drawingPane.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown() && event.getSceneY() > drawingPane.getMinHeight()) {
                drawingPane.setMinHeight(event.getSceneY());
            }
        });
        
        for(int i = 0; i < 500; i+=10) {
        	drawingPane.getChildren().add(new Line(0, i, 20, i));
        }
        
        drawingPane.getChildren().addAll(new GradeShapeGroup().get());
        Scene scene = new Scene(scrollPane);
        stage.setMinWidth(100);
        stage.setMinHeight(100);
        stage.setScene(scene);
        stage.show();
    }
}