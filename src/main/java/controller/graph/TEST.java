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
        
        Grade grade = new Grade("B", 80, java.awt.Color.BLUE);
        
        GradeShape obj = new GradeShape(250, 250, grade);
        
        root.getChildren().addAll(obj.get());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
