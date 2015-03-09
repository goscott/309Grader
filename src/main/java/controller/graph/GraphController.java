package controller.graph;

import java.util.ArrayList;
import java.util.Collections;

import controller.GraderPopup;
import controller.roster.GradebookController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.curve.Grade;
import model.driver.Grader;
import model.roster.Roster;
import model.roster.Student;

/**
 * Controls the Graphs tab visuals and interface
 * @author Frank Poole
 */
public class GraphController {
	@FXML
	/** Students label */
	private Label studentsLabel;
	@FXML
	/** Grade Label */
	private Label gradeLabel;
	@FXML
	/** Percentage label */
	private Label percentageLabel;
	@FXML
	/** Curve Table */
	private TableView<Grade> curveTable;
	@FXML
	/** Grade column */
	private TableColumn<Grade, String> gradeCol;
	@FXML
	/** Minimum column */
	private TableColumn<Grade, String> minCol;
	@FXML
	/** Students grade table */
	private TableView<StudentsPerGradeObject> studentTable;
	@FXML
	/** Students per grade - grade */
	private TableColumn<StudentsPerGradeObject, String> spg_grade;
	@FXML
	/** Students per grade - students */
	private TableColumn<StudentsPerGradeObject, String> spg_students;
	@FXML
	/** Students per grade percentage */
	private TableColumn<StudentsPerGradeObject, String> spg_percentage;
	@FXML
	/** Pie Chart */
	private PieChart pie_chart;
	@FXML
	/** New grade button */
	private Button newGradeButton;
	@FXML
	/** Grade adjustment sldier */
	private Slider slider;
	@FXML
	/** The histogram pane **/
	private StackPane histoPane;
	@FXML
	/** The right-most pane **/
	private TitledPane rightPane;
	@FXML
	/** Select grade drop down menu */
	private ComboBox<String> gradeSelectDropdown;
	/** Graph controller reference */
	private static GraphController currentInstance;
	/** Current roster curve data */
	private ObservableList<Grade> curveData;
	/** Current roster grade data */
	private ObservableList<StudentsPerGradeObject> gradeData;
	/** Pie chart is visible */
	private boolean showPie;

	/**
	 * Initialize the graph tab view.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		currentInstance = this;

		curveData = FXCollections.observableArrayList();
		gradeData = FXCollections.observableArrayList();

		gradeCol.setCellValueFactory(new Callback() {
			public SimpleStringProperty call(
					CellDataFeatures<Grade, String> param) {
				return new SimpleStringProperty(param.getValue().getName());
			}

			public Object call(Object param) {
				return call((CellDataFeatures<Grade, String>) (param));
			}
		});

		minCol.setCellValueFactory(new Callback() {
			public SimpleStringProperty call(
					CellDataFeatures<Grade, String> param) {
				return new SimpleStringProperty(param.getValue().value() + "");
			}

			public Object call(Object param) {
				return call((CellDataFeatures<Grade, String>) (param));
			}
		});
		spg_grade.setCellValueFactory(new Callback() {
			public SimpleStringProperty call(
					CellDataFeatures<StudentsPerGradeObject, String> param) {
				return new SimpleStringProperty(param.getValue().getGrade());
			}

			public Object call(Object param) {
				return call((CellDataFeatures<StudentsPerGradeObject, String>) (param));
			}
		});
		spg_students.setCellValueFactory(new Callback() {
			public SimpleStringProperty call(
					CellDataFeatures<StudentsPerGradeObject, String> param) {
				return new SimpleStringProperty(param.getValue()
						.getNumStudents() + "");
			}

			public Object call(Object param) {
				return call((CellDataFeatures<StudentsPerGradeObject, String>) (param));
			}
		});
		spg_percentage.setCellValueFactory(new Callback() {
			public SimpleStringProperty call(
					CellDataFeatures<StudentsPerGradeObject, String> param) {
				return new SimpleStringProperty(param.getValue()
						.getPercentage());
			}

			public Object call(Object param) {
				return call((CellDataFeatures<StudentsPerGradeObject, String>) (param));
			}
		});

		slider.setVisible(false);
		slider.setMajorTickUnit(1);
		slider.setMinorTickCount(0);
		slider.setSnapToTicks(true);
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
				if (slider.isVisible()) {
					Grade grade = Grader.getCurve().getGrade(
							gradeSelectDropdown.getValue());
					Grader.getCurve().adjust(grade, new_val.doubleValue());
					GradebookController.edited = true;
					Histogram.refresh();
				}

				updateCurveTable();
				updateGradeTable();
				updateStatsBox();
				updatePieChart();
				createGradeSeries();
			}
		});
		histoPane.getChildren().clear();
		histoPane.getChildren().add(Histogram.get());
	}

	/**
	 * Update the graph tab view with class roster information.
	 */
	public void update() {
		updateStatsBox();
		updatePieChart();
		createGradeSeries();

		// populate combobox
		gradeSelectDropdown.getSelectionModel().clearSelection();
		updateCombobox();

		slider.setVisible(false);

		updateCurveTable();
		updateGradeTable();
	}

	/**
	 * Gets the singleton instance of this controller
	 */
	public static GraphController get() {
		return currentInstance;
	}
	
	/**
	 * Updates the stats box.
	 */
	public void updateStatsBox() {
		studentsLabel.setText(Integer
				.toString(Grader.getRoster().numStudents()));
		gradeLabel.setText(Grader.getRoster().getLetterAverage());
		percentageLabel.setText(Double.toString(Grader.getRoster()
				.getPercentAverage()));
	}

	/**
	 * Updates the curve display.
	 */
	@SuppressWarnings("unchecked")
	public void updateCurveTable() {

		curveData.clear();
		ArrayList<Grade> gradesSorted = (ArrayList<Grade>) Grader.getCurve()
				.getGrades().clone();
		Collections.reverse(gradesSorted);
		curveData.addAll(gradesSorted);
		curveTable.setItems(curveData);
	}

	/**
	 * Update the grade table.
	 */
	@SuppressWarnings("unchecked")
	public void updateGradeTable() {
		gradeData.clear();
		int numStudents;
		int percentage;
		ArrayList<Grade> gradesSorted = (ArrayList<Grade>) Grader.getCurve()
				.getGrades().clone();
		Collections.reverse(gradesSorted);
		for (Grade grade : gradesSorted) {
			numStudents = Grader.getRoster().getStudentsByGrade(grade).size();
			// avoid divide by zero error
			if (Grader.getRoster().getStudents().size() > 0) {
				percentage = (int) (((double) numStudents / (double) Grader
						.getRoster().getStudents().size()) * 100);
			} else {
				percentage = 0;
			}

			gradeData.add(new StudentsPerGradeObject(grade.getName(),
					numStudents, percentage + "%"));
		}
		studentTable.setItems(gradeData);
	}

	/**
	 * Switch the the graph not in focus (either bar chart or pie chart).
	 */
	@FXML
	public void switchGraph() {
		rightPane.setText("Breakdown");
		if (showPie) {
			// switch to lineChart
			pie_chart.setVisible(false);
			histoPane.setVisible(true);
			Histogram.refresh();
		} else {
			histoPane.setVisible(false);
			pie_chart.setVisible(true);
		}
		showPie = !showPie;
	}

	/**
	 * Update the pie chart with the the student roster data.
	 */
	private void updatePieChart() {
		ObservableList<PieChart.Data> pieChartData = FXCollections
				.observableArrayList();
		Roster selectedCourse = Grader.getRoster();
		int temp;

		for (Grade grade : selectedCourse.getCurve().getGrades()) {
			temp = selectedCourse.getStudentsByGrade(grade).size();
			if (temp > 0) {
				pieChartData.add(new PieChart.Data(grade.getName(), temp));
			}
		}
		pie_chart.setData(pieChartData);
		pie_chart.setTitle("Grade Breakdown");
	}

	/**
	 * Populate the combo box with current roster data.
	 */
	private void updateCombobox() {
		gradeSelectDropdown.getItems().clear();
		ArrayList<Grade> list = Grader.getCurve().getGrades();
		Collections.reverse(list);
		for (Grade grade : list) {
			if (grade != null) {
				gradeSelectDropdown.getItems().add(grade.getName());
			}
		}
	}

	/**
	 * Create a new grade series to populate the bar chart.
	 */
	private void createGradeSeries() {
		XYChart.Series<Integer, String> fSeries = new XYChart.Series<Integer, String>();
		XYChart.Series<Integer, String> dSeries = new XYChart.Series<Integer, String>();
		XYChart.Series<Integer, String> cSeries = new XYChart.Series<Integer, String>();
		XYChart.Series<Integer, String> bSeries = new XYChart.Series<Integer, String>();
		XYChart.Series<Integer, String> aSeries = new XYChart.Series<Integer, String>();
		fSeries.setName("F");
		dSeries.setName("D");
		cSeries.setName("C");
		bSeries.setName("B");
		aSeries.setName("A");
		int maxVal = 0;

		int[] scores = new int[101];

		for (int index = 0; index < scores.length; index++) {
			scores[index] = 0;
		}

		for (Student student : Grader.getRoster().getStudents()) {
			scores[(int) student.getTotalPercentage()]++;
		}

		for (int i = 0; i < (int) Grader.getCurve().getGrade("D").value(); ++i) {
			if (scores[i] > 0) {
				XYChart.Data<Integer, String> data = new XYChart.Data<Integer, String>(
						scores[i], Integer.toString(i));
				fSeries.getData().add(data);
				maxVal = (maxVal < scores[i]) ? scores[i] : maxVal;
			}
		}
		for (int i = (int) Grader.getCurve().getGrade("D").value(); i < (int) Grader
				.getCurve().getGrade("C").value(); ++i) {
			if (scores[i] > 0) {
				XYChart.Data<Integer, String> data = new XYChart.Data<Integer, String>(
						scores[i], Integer.toString(i));
				dSeries.getData().add(data);
				maxVal = (maxVal < scores[i]) ? scores[i] : maxVal;
			}
		}
		for (int i = (int) Grader.getCurve().getGrade("C").value(); i < (int) Grader
				.getCurve().getGrade("B").value(); ++i) {
			if (scores[i] > 0) {
				XYChart.Data<Integer, String> data = new XYChart.Data<Integer, String>(
						scores[i], Integer.toString(i));
				cSeries.getData().add(data);
				maxVal = (maxVal < scores[i]) ? scores[i] : maxVal;
			}
		}
		for (int i = (int) Grader.getCurve().getGrade("B").value(); i < (int) Grader
				.getCurve().getGrade("A").value(); ++i) {
			if (scores[i] > 0) {
				XYChart.Data<Integer, String> data = new XYChart.Data<Integer, String>(
						scores[i], Integer.toString(i));
				bSeries.getData().add(data);
				maxVal = (maxVal < scores[i]) ? scores[i] : maxVal;
			}
		}
		for (int i = (int) Grader.getCurve().getGrade("A").value(); i <= 100; ++i) {
			if (scores[i] > 0) {
				XYChart.Data<Integer, String> data = new XYChart.Data<Integer, String>(
						scores[i], Integer.toString(i));
				aSeries.getData().add(data);
				maxVal = (maxVal < scores[i]) ? scores[i] : maxVal;
			}
		}
	}

	/**
	 * Handles adding a new grade (bringing up the dialog)
	 */
	@FXML
	private void handleNewGrade(ActionEvent event) {
		Stage stage = GraderPopup.getPopupStage("view/graph/NewGrade.fxml");
		newGradeButton.setDisable(true);
		stage.setOnHidden(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent arg0) {
				newGradeButton.setDisable(false);
			}
		});
		stage.show();
	}

	/**
	 * Handles the selection of a new grade to edit in the dropdown menu
	 */
	@FXML
	private void selectNewGrade(ActionEvent event) {
		Grade grade = Grader.getCurve()
				.getGrade(gradeSelectDropdown.getValue());
		if (grade != null) {
			slider.setVisible(false);
			slider.setValue(grade.value());
			slider.setMax(100);
			if (Grader.getCurve().getGradeAbove(grade) != null) {
				slider.setMax(Grader.getCurve().getGradeAbove(grade).value() - 1);
			}
			slider.setMin(0);
			if (Grader.getCurve().getGradeBelow(grade) != null) {
				slider.setMin(Grader.getCurve().getGradeBelow(grade).value() + 1);
			}
			slider.setValue(grade.value());
			slider.setVisible(true);
		}
	}

	/**
	 * A students per grade object represents a collection of students in the
	 * same grade category.
	 */
	private class StudentsPerGradeObject {
		private String grade;
		private int numStudents;
		private String percentage;

		/**
		 * Create a new students per grade object.
		 * @param newGrade the grade category
		 * @param newNumStudents the number of students
		 * @param newPercentage the grade percentage
		 */
		public StudentsPerGradeObject(String newGrade, int newNumStudents,
				String newPercentage) {
			grade = newGrade;
			numStudents = newNumStudents;
			percentage = newPercentage;
		}
		
		/**
		 * Return the grade representation.
		 * @return the grade representation
		 */
		public String getGrade() {
			return grade;
		}

		/**
		 * Return the number of students.
		 * @return the number of students
		 */
		public int getNumStudents() {
			return numStudents;
		}

		/**
		 * Return the grade percentage description.
		 * @return
		 */
		public String getPercentage() {
			return percentage;
		}
	}

	/**
	 * Allows other classes to update the tab
	 */
	public static void refresh() {
		currentInstance.update();
		Histogram.refresh();
	}
	
	/**
	 * Sets the given grade as selected
	 */
	public void setSelectedGrade(Grade grade) {
		if(grade != null) {
			rightPane.setText("Currently selected:   " + grade.getName());
		} else {
			rightPane.setText("Breakdown");
		}
	}
}