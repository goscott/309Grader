package controller.graph;

import model.curve.Grade;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Demonstrates a drag-and-drop feature.
 */
public class TEST extends Application {
	
    @Override public void start(Stage stage) {
        stage.setTitle("Drag and Drop Test");

        Group root = new Group();
        Scene scene = new Scene(root, 500, 500);
        
        Grade b = new Grade("B", 80, java.awt.Color.BLUE);
        Grade a = new Grade("A", 90, java.awt.Color.GREEN);
        
        root.getChildren().addAll(new GradeShape(250, 250, a).get());
        root.getChildren().addAll(new GradeShape(250, 300, b).get());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
