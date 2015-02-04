package controller.history;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import model.driver.Grader;
import model.history.CourseHistory;
import model.history.HistoryDB;
import model.roster.Roster;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
    private BorderPane history_view;
    
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
    
    @FXML
    private Label section_label;
    
    @FXML
    private Label course_label;
    
    @FXML
    private Button switch_graph;
    
    private int num = 1;
    
    private ToggleButton selectedSection = null;
    
    private HistoryDB history;
    
    public void initialize() {
        //load classes from history db
        class_selector.expandedPaneProperty().addListener(new 
                ChangeListener<TitledPane>() {
                    public void changed(ObservableValue<? extends TitledPane> ov, TitledPane old_val, TitledPane new_val) {
                        
                        //expanded 
                        if (class_selector.getExpandedPane() != null) {
                            switchToCourse();
                            course_label.setText(class_selector.getExpandedPane().getText() + ": View Under Construction");
                            
                            switch_graph.setVisible(true);
                        }
                        
                        //collapsed
                        else {
                            switchToIntro();
                            
                            if (selectedSection != null) {
                                selectedSection.setSelected(false);
                            }
                        }
                  }
            });
        
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Stuff", 13),
                new PieChart.Data("Things", 25),
                new PieChart.Data("Items", 10));
        pie_chart.setData(pieChartData);
        
        //this is where we would pull info from course history
        // to build the list of courses
        history = Grader.getHistoryDB();
        
        //populates history db with fake classes
        fillHistoryDB();
        
        for (CourseHistory course : history.getHistory()) {
            addClass(course);
        }
        
        /*
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();
        addClass();*/
        
        //fixes an issue with expanding titled panes getting cut off
        TitledPane padding = new TitledPane();
        padding.setVisible(false);
        class_selector.getPanes().add(padding);
    }
    
    public void addClass() {
        VBox content = new VBox();
        TitledPane toAdd;
        SectionButton placeHolder = new SectionButton("Section1");
        SectionButton placeHolder2 = new SectionButton("Section2");
        
        
        content.getChildren().add(placeHolder);
        VBox.setMargin(placeHolder, new Insets(0, 0, 10, 0));
        
        content.getChildren().add(placeHolder2);
        VBox.setMargin(placeHolder2, new Insets(0, 0, 10, 0));
        
        if (num < 10)
            toAdd = new TitledPane("CSC/CPE 30" + num++, content);
        else
            toAdd = new TitledPane("CSC/CPE 3" + num++, content);
        toAdd.setAnimated(true);
        toAdd.setPadding(new Insets(10, 20, 0, 5));
        class_selector.getPanes().add(toAdd);
    }
    
    public void addClass(CourseHistory course) {
        VBox content = new VBox();
        TitledPane toAdd;
        
        for (Roster section : course.getHistory()) {
            SectionButton temp = new SectionButton(section.getQuarter() + " " + section.getStartDate().get(Calendar.YEAR) + " Section0" + section.getSection());
            content.getChildren().add(temp);
            VBox.setMargin(temp, new Insets(0, 0, 10, 0));
        }
        
        toAdd = new TitledPane(course.getCourseName(), content);
        
        toAdd.setAnimated(true);
        toAdd.setPadding(new Insets(10, 20, 0, 5));
        class_selector.getPanes().add(toAdd);
    }
    
    private void fillHistoryDB() {
        //history.addRoster(new Roster(name, String instructor, int section, String quarter, Date startDate, Date endDate));
        Calendar start;
        
        start = Calendar.getInstance();
        start.set(2012, 4, 1);
        history.addRoster(new Roster("CSC/CPE 308", "Gene Fisher", 1, "Spring", start, null));
        history.addRoster(new Roster("CSC/CPE 309", "Gene Fisher", 1, "Spring", start, null));
        
        start = Calendar.getInstance();
        start.set(2013, 1, 1);
        history.addRoster(new Roster("CSC/CPE 308", "Gene Fisher", 1, "Winter", start, null));
        history.addRoster(new Roster("CSC/CPE 308", "Gene Fisher", 3, "Winter", start, null));
        history.addRoster(new Roster("CSC/CPE 309", "Gene Fisher", 1, "Winter", start, null));
        
        start = Calendar.getInstance();
        start.set(2013, 8, 1);
        history.addRoster(new Roster("CSC/CPE 309", "Gene Fisher", 1, "Fall", start, null));
        history.addRoster(new Roster("CSC/CPE 305", "John Dalbey", 1, "Fall", start, null));
        
        start = Calendar.getInstance();
        start.set(2014, 1, 1);
        history.addRoster(new Roster("CSC/CPE 349", "Timothy Kearns", 1, "Winter", start, null));
        
    }
    
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
    
    private class SectionButton extends ToggleButton {
        
        public SectionButton() {
            initialize();
        }
        
        public SectionButton(String name) {
            super(name);
            initialize();
        }
        
        public void initialize() {
            this.setFocusTraversable(false);
            this.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    
                    // on
                    if (((ToggleButton)event.getSource()).isSelected()) {
                        switchToSection();
                        
                        if (selectedSection != null) {
                            selectedSection.setSelected(false); //deactivate other button
                        }
                        
                        selectedSection = (ToggleButton) event.getSource();
                        
                        section_label.setText(selectedSection.getText() + ": View Under Construction.");
                        
                        switch_graph.setVisible(false);
                    }
                    
                    // off
                    else {
                        switchToCourse();
                        selectedSection = null;
                        switch_graph.setVisible(true);
                    }
                }
            });
        }
        
    }
}
