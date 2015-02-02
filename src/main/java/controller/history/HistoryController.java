package controller.history;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controlls the history view.
 * @author Mason Stevenson
 *
 */
public class HistoryController {
    
    /**
     * Shows course history info.
     */
    @FXML
    private HBox history_view;
    
    /**
     * Shows completed (non-editable) gradebook for this course.
     */
    @FXML 
    private BorderPane gradebook_view;
    
    /**
     * Switches to gradebook view.
     */
    @FXML
    private Button button_gradebook;
    
    /**
     * Switches to history view.
     */
    @FXML
    private Button button_history;
    
    @FXML
    private Accordion class_selector;
    
    @FXML
    private BorderPane intro_view;
    
    @FXML
    private BorderPane course_view;
    
    @FXML
    private BorderPane section_view;
    
    @FXML
    private LineChart line_chart;
    
    @FXML
    private PieChart pie_chart;
    
    private int num = 1;
    
    public void initialize() {
        //load classes from history db
        class_selector.expandedPaneProperty().addListener(new 
                ChangeListener<TitledPane>() {
                    public void changed(ObservableValue<? extends TitledPane> ov, TitledPane old_val, TitledPane new_val) {
                        if (class_selector.getExpandedPane() != null) {
                            switchToCourse();
                        }
                        
                        else {
                            switchToIntro();
                        }
                  }
            });
        
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Stuff", 13),
                new PieChart.Data("Things", 25),
                new PieChart.Data("Items", 10));
        pie_chart.setData(pieChartData);
    }
    
    public void addClass() {
        VBox content = new VBox();
        TitledPane toAdd;
        ToggleButton placeHolder = new ToggleButton("PlaceHolder");
        placeHolder.setFocusTraversable(false);
        
        placeHolder.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                if (((ToggleButton)event.getSource()).isSelected()) {
                    switchToSection();
                }
                
                else {
                    switchToCourse();
                }
            }
        });
        
        content.getChildren().add(placeHolder);
        toAdd = new TitledPane("Dynamic Pane #" + num++, content);
        toAdd.setAnimated(true);
        toAdd.setPadding(new Insets(0, 10, 10, 10));
        class_selector.getPanes().add(toAdd);
    }
    
    /*
    public void addSection() {
        VBox content;
        
        for (TitledPane pane : class_selector.getPanes()) {
            if (pane.getText().equals(targetCourse)) {
            }
        }
    }*/
    
    public void switchGraph() {
        if (line_chart.isVisible()) {
            switchToPieChart();
        }
        
        else {
            switchToLineChart();
        }
    }
    
    public void switchToLineChart() {
        line_chart.setVisible(true);
        pie_chart.setVisible(false);
    }
    
    public void switchToPieChart() {
        line_chart.setVisible(false);
        pie_chart.setVisible(true);
        
        
    }
    
    public void switchToIntro() {
        course_view.setVisible(false);
        intro_view.setVisible(true);
        section_view.setVisible(false);
    }
    
    public void switchToCourse() {
        course_view.setVisible(true);
        intro_view.setVisible(false);
        section_view.setVisible(false);
        
        switchToLineChart();
    }
    
    public void switchToSection() {
        course_view.setVisible(false);
        intro_view.setVisible(false);
        section_view.setVisible(true);
    }
    
    /**
     * Switches the view from history to gradebook.
     */
    public void switchToGradebook() {
        history_view.setVisible(false);
        gradebook_view.setVisible(true);
        section_view.setVisible(false);
        
        try {
            TableView<?> gradebookPage = (TableView<?>) FXMLLoader
                    .load(getClass().getClassLoader().getResource(
                            "view/roster/gradebook_screen.fxml"));
            gradebook_view.setCenter(gradebookPage);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Switches the view from gradebook to history.
     */
    public void switchToHistory() {
        history_view.setVisible(true);
        gradebook_view.setVisible(false);
    }
}
