package controller.history;

import java.util.Calendar;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.curve.Grade;
import model.driver.Debug;
import model.driver.Grader;
import model.history.CourseHistory;
import model.history.HistoryDB;
import model.history.QuarterAverage;
import model.roster.Roster;
import controller.GraderPopup;
import controller.roster.GradebookController;

/**
 * Controls the history view.
 * 
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

    /**
     * Allows users to select a course, entering course view.
     */
    @FXML
    private Accordion class_selector;

    /**
     * This shows some introduction text when the user first opens up the
     * history.
     */
    @FXML
    private BorderPane intro_view;

    /**
     * This pane shows aggregate info about a course.
     */
    @FXML
    private BorderPane course_view;

    /**
     * This pane shows info about a specific section.
     */
    @FXML
    private BorderPane section_view;

    /**
     * Class averages line chart for the course view.
     */
    @FXML
    private LineChart<String, Double> line_chart;

    /**
     * Grade breakdown pie chart for the course view.
     */
    @FXML
    private PieChart pie_chart;

    /**
     * Grade breakdown pie chart for the section view.
     */
    @FXML
    private PieChart section_pie;

    /**
     * Displays the currently selected section in the section view.
     */
    @FXML
    private Label section_label;

    /**
     * Displays the currently selected course in the course view.
     */
    @FXML
    private Label course_label;

    /**
     * Allows for switching between the line and pie charts in the course view.
     */
    @FXML
    private Button switch_graph;

    /**
     * Number of students in a course.
     */
    @FXML
    private Label label_students;

    /**
     * Number of sections in a course.
     */
    @FXML
    private Label label_sections;

    /**
     * Average class size for a course (avg of all sections).
     */
    @FXML
    private Label label_class;

    /**
     * Average grade for a course (avg of all sections).
     */
    @FXML
    private Label label_grade;

    /**
     * Holds course info.
     */
    @FXML
    private VBox course_info_box;

    /**
     * Holds section pie chart.
     */
    @FXML
    private FlowPane graph_box;

    /**
     * Holds section info.
     */
    @FXML
    private VBox section_info_box;

    /**
     * A section's instructor.
     */
    @FXML
    private Label section_instructor;

    /**
     * A section's number of students.
     */
    @FXML
    private Label section_students;

    /**
     * A section's number of assignments.
     */
    @FXML
    private Label section_assignments;

    /**
     * A section's curve.
     */
    @FXML
    private Label section_curve;

    /**
     * A section's start date.
     */
    @FXML
    private Label section_start_date;

    /**
     * A section's end date.
     */
    @FXML
    private Label section_end_date;

    /**
     * A reference to the currently selected togglebutton. (null if none is
     * selected)
     */
    private SectionButton selectedSection = null;

    /**
     * A reference to the active history database.
     */
    private HistoryDB history;

    public static HistoryController currentInstance;

    /**
     * Builds the history view.
     */
    public void initialize() {
        currentInstance = this;
        line_chart.setAnimated(false);
        line_chart.setTitle("Average Grade Per Quarter");

        initStyle();

        // load classes from history db
        class_selector.expandedPaneProperty().addListener(
                new ChangeListener<TitledPane>() {
                    public void changed(
                            ObservableValue<? extends TitledPane> ov,
                            TitledPane old_val, TitledPane new_val) {

                        // expanded
                        if (class_selector.getExpandedPane() != null) {
                            CourseHistory selectedCourse = history
                                    .getCourseHistory(class_selector
                                            .getExpandedPane().getText());

                            switchToCourse();
                            course_label.setText(class_selector
                                    .getExpandedPane().getText());

                            switch_graph.setVisible(true);
                            fillCharts(selectedCourse);
                        }

                        // collapsed
                        else {
                            switchToIntro();

                            switch_graph.setVisible(false);
                            button_gradebook.setVisible(false);

                            if (selectedSection != null) {
                                selectedSection.setStyle(
                                        "-fx-background-color: #2EA1B1, "
                                        + "linear-gradient(#2EA1B1, #74D7E5); "
                                        + "-fx-text-base-color: #ffffff;"
                                        + "-fx-background-radius: 3,2,1;"
                                        + "-fx-text-alignment: left;");
                                selectedSection.setSelected(false);
                                selectedSection = null;
                            }
                        }
                    }
                });

        loadClasses();
    }

    /**
     * Initializes this style changes for history.
     */
    private void initStyle() {

        course_view.setStyle("-fx-background-color: white;");

        history_view.setStyle("-fx-background-color: white;");

        course_info_box.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 2;" + "-fx-border-color: black;"
                + "-fx-background-color: white;");

        graph_box.setStyle("-fx-border-style: solid;" + "-fx-border-width: 2;"
                + "-fx-border-color: black;" + "-fx-background-color: white;");

        section_info_box.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 2;" + "-fx-border-color: black;"
                + "-fx-background-color: white;");
    }

    /**
     * Builds the line and piecharts for the course history view.
     */
    private void fillCharts(CourseHistory selectedCourse) {
        XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();
        series.setName("Average Grade Per Quarter");
        String[] grades = { "A", "B", "C", "D", "F" };
        int temp;

        for (QuarterAverage ave : selectedCourse.getAverages()) {
            series.getData().add(
                    new XYChart.Data<String, Double>(ave.getQuarter() + " "
                            + ave.getYear(), ave.getValue()));
        }

        line_chart.getData().clear();
        line_chart.getData().add(series);

        label_students.setText("Total Number of Students Taught: "
                + selectedCourse.getTotalStudents());
        label_sections.setText("Total Number of Sections: "
                + selectedCourse.getNumSectionsTaught());
        label_class.setText("Average Class Size: "
                + selectedCourse.getAverageClassSize());
        label_grade.setText("Average Grade: "
                + selectedCourse.getAverageGrade());

        ObservableList<PieChart.Data> pieChartData = FXCollections
                .observableArrayList();

        for (String grade : grades) {
            temp = selectedCourse.getStudentsByGrade(grade);
            if (temp > 0) {
                pieChartData.add(new PieChart.Data(grade, temp));
            }
        }
        pie_chart.setData(pieChartData);
        pie_chart.setTitle("Grade Breakdown Across All Quarters");

    }

    /**
     * Loads a list of courses into the course selector.
     */
    public void loadClasses() {
        Debug.log("HISTORY", "loading classes");
        class_selector.getPanes().clear();

        // this is where we would pull info from course history
        // to build the list of courses
        history = Grader.getHistoryDB();

        for (CourseHistory course : history.getHistory()) {
            addClass(course);
        }

        // fixes an issue with expanding titled panes getting cut off
        TitledPane padding = new TitledPane();
        padding.setVisible(false);
        class_selector.getPanes().add(padding);
    }

    /**
     * Adds a course the the course selector.
     */
    private void addClass(CourseHistory course) {
        VBox content = new VBox();
        TitledPane toAdd;

        for (Roster section : course.getHistory()) {
            SectionButton temp = new SectionButton(section);
            content.getChildren().add(temp);
            VBox.setMargin(temp, new Insets(0, 0, 10, 0));
        }

        content.setStyle("-fx-background-color: white;");
        toAdd = new TitledPane(course.getCourseName(), content);

        toAdd.setAnimated(true);
        toAdd.setPadding(new Insets(10, 20, 0, 5));
        class_selector.getPanes().add(toAdd);
    }

    /**
     * Switches from linechart to piechart in the course view.
     */
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

    private void switchToLineChart() {
        line_chart.setVisible(true);
        pie_chart.setVisible(false);
    }

    private void switchToPieChart() {
        line_chart.setVisible(false);
        pie_chart.setVisible(true);

    }

    /**
     * Switches the main view to the Intro view.
     */
    public void switchToIntro() {
        course_view.setVisible(false);
        intro_view.setVisible(true);
        section_view.setVisible(false);
    }

    /**
     * Switches the main view to the Course view.
     */
    public void switchToCourse() {
        course_view.setVisible(true);
        intro_view.setVisible(false);
        section_view.setVisible(false);

        switchToLineChart();
    }

    /**
     * Switches the main view to the Section view.
     */
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

        BorderPane gradebookPage = (BorderPane) GraderPopup
                .getResource("view/roster/gradebook_screen.fxml");
        gradebook_view.setCenter(gradebookPage);
        GradebookController.getControllerTwo().showRoster(
                selectedSection.getSection());
        GradebookController.getControllerTwo().initialize();
        GradebookController.getControllerTwo().deactivateCellEditing();
    }

    /**
     * Switches the view from gradebook to history.
     */
    public void switchToHistory() {
        history_view.setVisible(true);
        gradebook_view.setVisible(false);
        section_view.setVisible(true);
    }

    /**
     * A button containing a reference to the specific Roster (section) that it
     * references.
     *
     */
    private class SectionButton extends ToggleButton {

        private Roster section;

        public SectionButton(Roster newSection) {
            super(newSection.getQuarter() + " "
                    + newSection.getStartDate().get(Calendar.YEAR)
                    + " Section0" + newSection.getSection());
            section = newSection;
            this.setStyle("-fx-background-color: #2EA1B1, linear-gradient(#2EA1B1, #74D7E5); "
                    + "-fx-text-base-color: #ffffff;"
                    + "-fx-background-radius: 3,2,1;"
                    + "-fx-text-alignment: left;");
            initialize();
        }

        public Roster getSection() {
            return section;
        }

        public void initialize() {
            this.setFocusTraversable(false);
            this.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                    // on
                    if (((ToggleButton) event.getSource()).isSelected()) {
                        switchToSection();

                        ((ToggleButton) event.getSource())
                                .setStyle("-fx-background-color: #74D7E5, "
                                        + "linear-gradient(#74D7E5, #A1DDE5);"
                                        + "-fx-text-base-color: #ffffff;"
                                        + "-fx-background-radius: 3,2,1;"
                                        + "-fx-text-alignment: left;");

                        if (selectedSection != null) {
                            selectedSection
                                    .setStyle("-fx-background-color: #2EA1B1, "
                                            + "linear-gradient(#2EA1B1, #74D7E5); "
                                            + "-fx-text-base-color: #ffffff;"
                                            + "-fx-background-radius: 3,2,1;"
                                            + "-fx-text-alignment: left;");
                            selectedSection.setSelected(false); // deactivate
                                                                // other button

                        }

                        selectedSection = (SectionButton) event.getSource();

                        section_label.setText(selectedSection.getText());

                        switch_graph.setVisible(false);

                        section_instructor.setText("Instructor: "
                                + section.getInstructorId());
                        section_students
                                .setText("Number of Students (x passed, y failed): "
                                        + section.getStudents().size()
                                        + "("
                                        + section.getPassingNum()
                                        + ","
                                        + section.getFailingNum() + ")");
                        section_assignments.setText("Number of assignments: "
                                + section.getAssignments().size());
                        section_curve.setText("Curve: "
                                + section.getCurve().toString());
                        section_start_date.setText("Start Date: "
                                + section.getStartDateString());
                        section_end_date.setText("End Date: "
                                + section.getEndDateString());

                        fillSectionPie(section);

                        button_gradebook.setVisible(true);
                    }

                    // off
                    else {
                        switchToCourse();
                        selectedSection = null;
                        switch_graph.setVisible(true);
                        button_gradebook.setVisible(false);

                    }
                }
            });
        }
    }

    /**
     * Builds the piechart located in the section view.
     */
    private void fillSectionPie(Roster section) {
        int temp;

        ObservableList<PieChart.Data> pieChartData = FXCollections
                .observableArrayList();

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
