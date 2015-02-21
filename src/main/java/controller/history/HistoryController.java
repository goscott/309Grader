package controller.history;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import model.curve.Grade;
import model.driver.Debug;
import model.driver.Grader;
import model.history.CourseHistory;
import model.history.HistoryDB;
import model.roster.Roster;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
    private PieChart section_pie;
    
    @FXML
    private Label section_label;
    
    @FXML
    private Label course_label;
    
    @FXML
    private Button switch_graph;
    
    @FXML
    private CheckBox hide_course;
    
    @FXML
    private Label label_students;
    
    @FXML
    private Label label_sections;
    
    @FXML
    private Label label_class;
    
    @FXML
    private Label label_grade;
    
    @FXML
    private VBox course_info_box;
    
    @FXML
    private FlowPane graph_box;
    
    @FXML
    private VBox section_info_box;
    
    @FXML
    private Label section_instructor;
    
    @FXML
    private Label section_students;
    
    @FXML
    private Label section_assignments;
    
    @FXML
    private Label section_curve;
    
    @FXML
    private Label section_start_date;
    
    @FXML
    private Label section_end_date;
    
    private int num = 1;
    
    private ToggleButton selectedSection = null;
    
    private HistoryDB history;
    
    private boolean firstTime = true;
    
    public void initialize() {
        Grader.setHistoryController(this);
        line_chart.setAnimated(false);
        line_chart.setTitle("Average Grade Per Quarter");
        
        initStyle();
        
        //load classes from history db
        class_selector.expandedPaneProperty().addListener(new 
                ChangeListener<TitledPane>() {
                    public void changed(ObservableValue<? extends TitledPane> ov, TitledPane old_val, TitledPane new_val) {
                        
                        //expanded 
                        if (class_selector.getExpandedPane() != null) {
                            CourseHistory selectedCourse = history.getCourseHistory(class_selector.getExpandedPane().getText());
                            
                            switchToCourse();
                            course_label.setText(class_selector.getExpandedPane().getText());
                            
                            switch_graph.setDisable(false);
                            hide_course.setDisable(false);
                            
                            fillCharts(selectedCourse);
                        }
                        
                        //collapsed
                        else {
                            switchToIntro();
                            
                            switch_graph.setDisable(true);
                            hide_course.setDisable(true);
                            
                            if (selectedSection != null) {
                                selectedSection.setSelected(false);
                                selectedSection = null;
                            }
                        }
                  }
            });
        
        loadClasses();
    }
    
    private void initStyle() {
        
        course_view.setStyle("-fx-background-color: grey;");
        
        course_label.setStyle(
                "-fx-text-fill: white;");
                //"-fx-border-style: solid;" +
                //"-fx-border-width: 2;" + 
                //"-fx-border-color: black;" +
                //"-fx-background-color: white;");
        
        course_info_box.setStyle(
                "-fx-border-style: solid;" +
                "-fx-border-width: 2;" + 
                "-fx-border-color: black;" +
                "-fx-background-color: white;");
        
        graph_box.setStyle(
                "-fx-border-style: solid;" +
                "-fx-border-width: 2;" + 
                "-fx-border-color: black;" +
                "-fx-background-color: white;");
        
        section_info_box.setStyle(
                "-fx-border-style: solid;" +
                "-fx-border-width: 2;" + 
                "-fx-border-color: black;" +
                "-fx-background-color: white;");
    }
    
    private void fillCharts(CourseHistory selectedCourse) {
        XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
        series.setName("Average Grade Per Quarter");
        int count = 0;
        String[] grades = {"A", "B", "C", "D", "F"};
        int temp;
        
        for (double val : selectedCourse.getAverages()) {
            count++;
            series.getData().add(new XYChart.Data<String, Double>("Quarter " + count, val));
        }
        
        line_chart.getData().clear();
        line_chart.getData().add(series);
        
        label_students.setText("Total Number of Students Taught: " + selectedCourse.getTotalStudents());
        label_sections.setText("Total Number of Sections: " + selectedCourse.getNumSectionsTaught());
        label_class.setText("Average Class Size: " + selectedCourse.getAverageClassSize());
        label_grade.setText("Average Grade: " + selectedCourse.getAverageGrade());
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        for (String grade : grades) {
            temp = selectedCourse.getStudentsByGrade(grade);
            if (temp > 0) {
                pieChartData.add(new PieChart.Data(grade, temp));
            }
        }
        pie_chart.setData(pieChartData);
        pie_chart.setTitle("Grade Breakdown Across All Quarters");
    }
    
    public void loadClasses() {
        Debug.log("HISTORY", "loading classes");
        class_selector.getPanes().clear();
        
        //this is where we would pull info from course history
        // to build the list of courses
        history = Grader.getHistoryDB();
        
        for (CourseHistory course : history.getHistory()) {
            addClass(course);
        }
        
        //fixes an issue with expanding titled panes getting cut off
        TitledPane padding = new TitledPane();
        padding.setVisible(false);
        class_selector.getPanes().add(padding);
    }
    
    public void call() {
        //JOptionPane.showMessageDialog(null, "history was called");
        class_selector.getPanes().clear();
    }
    
    public void addClass(CourseHistory course) {
        VBox content = new VBox();
        TitledPane toAdd;
        
        for (Roster section : course.getHistory()) {
            SectionButton temp = new SectionButton(section);
            content.getChildren().add(temp);
            VBox.setMargin(temp, new Insets(0, 0, 10, 0));
        }
        
        toAdd = new TitledPane(course.getCourseName(), content);
        
        toAdd.setAnimated(true);
        toAdd.setPadding(new Insets(10, 20, 0, 5));
        class_selector.getPanes().add(toAdd);
    }
    
    public void switchGraph() {
        if (line_chart.isVisible()) {
            switchToPieChart();
            switch_graph.setText("View Linechart");
        }
        
        else {
            switchToLineChart();
            switch_graph.setText("View Piechart");
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
        
        private Roster section;
        
        public SectionButton(Roster newSection) {
            super(newSection.getQuarter() + " " + newSection.getStartDate().get(Calendar.YEAR) + " Section0" + newSection.getSection());
            section = newSection;
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
                        
                        section_label.setText(selectedSection.getText());
                        
                        switch_graph.setDisable(true);
                        
                        section_instructor.setText("Instructor: " + section.getInstructor());
                        section_students.setText("Number of Students (x passed, y failed): " + section.getStudents().size() + "(" + section.getPassingNum() + "," + section.getFailingNum() + ")");
                        section_assignments.setText("Number of assignments: " + section.getAssignments().size());
                        section_curve.setText("Curve: " + section.getCurve().toString());
                        section_start_date.setText("Start Date: " + section.getStartDateString());
                        section_end_date.setText("End Date: " + section.getEndDateString());
                        
                        fillSectionPie(section);
                    }
                    
                    // off
                    else {
                        switchToCourse();
                        selectedSection = null;
                        switch_graph.setDisable(false);
                    }
                }
            });
        }
    }
    
    private void fillSectionPie(Roster section) {
        int temp;
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        for (Grade grade : section.getCurve().getGrades()) {
            temp = section.getStudentsByGrade(grade).size();
            
            if (temp > 0) {
                pieChartData.add(new PieChart.Data(grade.getName(), temp));
            }
        }
        
        if (section_pie != null) {
            section_pie.setData(pieChartData);
        }
    }
}
