package controller.graph;

import model.curve.Grade;
import model.driver.Grader;
import model.roster.Student;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controls the Graphs tab visuals and interface
 * @author Frank Poole
 */
public class GraphController {
    @FXML
    private Label studentsLabel;
    @FXML
    private Label gradeLabel;
    @FXML
    private Label percentageLabel;
    @FXML
    private TableView<Grade> curveTable;
    @FXML
    private TableColumn<Grade, String> gradeCol;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private CategoryAxis yAxis;
    @FXML
    private BarChart<Number, String> barChart;
    
    /**
     * Initialize the graph tab view.
     */
    public void initialize() {
        this.update();
    }
    
    /**
     * Update the graph tab view with class roster information.
     */
    public void update() {
        studentsLabel.setText(Integer.toString(Grader.getRoster().numStudents()));
        gradeLabel.setText(Grader.getRoster().getLetterAverage());
        percentageLabel.setText(Double.toString(Grader.getRoster().getPercentAverage()));
        createGradeSeries();
    }
    
    private void createGradeSeries() {
        XYChart.Series<Number, String> fSeries = new XYChart.Series<Number, String>();
        XYChart.Series<Number, String> dSeries = new XYChart.Series<Number, String>();
        XYChart.Series<Number, String> cSeries = new XYChart.Series<Number, String>();
        XYChart.Series<Number, String> bSeries = new XYChart.Series<Number, String>();
        XYChart.Series<Number, String> aSeries = new XYChart.Series<Number, String>();
        fSeries.setName("F");
        dSeries.setName("D");
        cSeries.setName("C");
        bSeries.setName("B");
        aSeries.setName("A");
        
        double[] scores = new double[101];
        for (Student student : Grader.getRoster().getStudents()) {
            scores[(int) student.getTotalPercentage()]++;
        }
        
        for (int i = 0; i < 60; ++i) {
            XYChart.Data<Number, String> data = new XYChart.Data<Number, String>(scores[i], Integer.toString(i));
            fSeries.getData().add(data);
        }
        for (int i = 60; i < 70; ++i) {
            XYChart.Data<Number, String> data = new XYChart.Data<Number, String>(scores[i], Integer.toString(i));
            dSeries.getData().add(data);
        }
        for (int i = 70; i < 80; ++i) {
            XYChart.Data<Number, String> data = new XYChart.Data<Number, String>(scores[i], Integer.toString(i));
            cSeries.getData().add(data);
        }
        for (int i = 80; i < 90; ++i) {
            XYChart.Data<Number, String> data = new XYChart.Data<Number, String>(scores[i], Integer.toString(i));
            bSeries.getData().add(data);
        }
        for (int i = 90; i <= 100; ++i) {
            XYChart.Data<Number, String> data = new XYChart.Data<Number, String>(scores[i], Integer.toString(i));
            aSeries.getData().add(data);
        }
        
        barChart.getData().add(fSeries);
        barChart.getData().add(dSeries);
        barChart.getData().add(cSeries);
        barChart.getData().add(bSeries);
        barChart.getData().add(aSeries);
    }
}
