package roster;

/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 */


import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;


/**
 * Tests the gradebook GUI
 * @author Gavin Scott
 *
 */
public class GradeBookTester extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(GradeBookTester.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
        	BorderPane page = (BorderPane) FXMLLoader.load(getClass().getResource("resources/gradebook_screen.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            /*scene.widthProperty().addListener(new ChangeListener<Number>() {
				public void stateChanged(ChangeEvent arg0) {
					// TODO Auto-generated method stub
					System.out.println("changed width");
				}
            });*/
            
        } catch (Exception ex) {
            Logger.getLogger(GradeBookTester.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}