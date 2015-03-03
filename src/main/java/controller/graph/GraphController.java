package controller.graph; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import controller.GraderPopup;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.curve.Grade;
import model.driver.Debug;
import model.driver.Grader;
import model.roster.Roster;
import model.roster.Student;

/**
 * Controls the Graphs tab visuals and interface
 * 
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
    private TableColumn<Grade, String> minCol;
	@FXML
    private TableView<StudentsPerGradeObject> studentTable;
	@FXML
	private TableColumn<StudentsPerGradeObject, String> spg_grade;
	@FXML
    private TableColumn<StudentsPerGradeObject, String> spg_students;
	@FXML
    private TableColumn<StudentsPerGradeObject, String> spg_percentage;
	@FXML
	private NumberAxis xAxis;
	@FXML
	private CategoryAxis yAxis;
	@FXML
	private BarChart<Integer, String> barChart;
	@FXML
	private PieChart pie_chart;
	@FXML
	private ScrollPane bar_pane;
	@FXML
	private Button newGradeButton;
	@FXML
	private Slider slider;
	@FXML
	private ComboBox<String> gradeSelectDropdown;
	
	private static GraphController currentInstance;
	
	private ObservableList<Grade> curveData;
	
	private ObservableList<StudentsPerGradeObject> gradeData;
	
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
            public SimpleStringProperty call(CellDataFeatures<Grade, String> param) {
                return new SimpleStringProperty(param.getValue().getName()); 
            }

            public Object call(Object param) {
                return call((CellDataFeatures<Grade, String>) (param));
            }
        });
		
		minCol.setCellValueFactory(new Callback() {
            public SimpleStringProperty call(CellDataFeatures<Grade, String> param) {
                return new SimpleStringProperty(param.getValue().value() + ""); 
            }

            public Object call(Object param) {
                return call((CellDataFeatures<Grade, String>) (param));
            }
        });
		spg_grade.setCellValueFactory(new Callback() {
            public SimpleStringProperty call(CellDataFeatures<StudentsPerGradeObject, String> param) {
                return new SimpleStringProperty(param.getValue().getGrade()); 
            }

            public Object call(Object param) {
                return call((CellDataFeatures<StudentsPerGradeObject, String>) (param));
            }
        });
		spg_students.setCellValueFactory(new Callback() {
            public SimpleStringProperty call(CellDataFeatures<StudentsPerGradeObject, String> param) {
                return new SimpleStringProperty(param.getValue().getNumStudents() + ""); 
            }

            public Object call(Object param) {
                return call((CellDataFeatures<StudentsPerGradeObject, String>) (param));
            }
        });
		spg_percentage.setCellValueFactory(new Callback() {
            public SimpleStringProperty call(CellDataFeatures<StudentsPerGradeObject, String> param) {
                return new SimpleStringProperty(param.getValue().getPercentage()); 
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
                    Grade grade = Grader.getCurve().getGrade(gradeSelectDropdown.getValue());
                    Grader.getCurve().adjust(grade,
                            new_val.doubleValue());
                }
                
                updateCurveTable();
                updateGradeTable();
                updateStatsBox();
                updatePieChart();
                createGradeSeries();
            }
        });
        // only do this when change is finished
        slider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isNowChanging) {
                if (!isNowChanging) {
                	Grade grade = Grader.getCurve().getGrade(gradeSelectDropdown.getValue());
                    if(slider.getValue() == Grader.getCurve().getGradeAbove(grade).value()
                    		|| slider.getValue() == Grader.getCurve().getGradeBelow(grade).value()) {
                    	Grader.getCurve().remove(grade);
                    	update();
                    }
                }
            }
        });
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
	    ArrayList<Grade> gradesSorted = (ArrayList<Grade>) Grader.getCurve().getGrades().clone();
	    Collections.reverse(gradesSorted);
	    curveData.addAll(gradesSorted);
	    curveTable.setItems(curveData);
	}
	
	@SuppressWarnings("unchecked")
    public void updateGradeTable() {
	    gradeData.clear();
	    int numStudents;
	    int percentage;
	    ArrayList<Grade> gradesSorted = (ArrayList<Grade>) Grader.getCurve().getGrades().clone();
	    Collections.reverse(gradesSorted);
	    for (Grade grade : gradesSorted) {
	        numStudents = Grader.getRoster().getStudentsByGrade(grade).size();
	        
	        //avoid divide by zero error
	        if (Grader.getRoster().getStudents().size() > 0) {
	            percentage = (int)(((double) numStudents / (double)Grader.getRoster().getStudents().size()) * 100);
	        }
	        
	        else {
	            percentage = 0;
	        }
	        
	        gradeData.add(new StudentsPerGradeObject(grade.getName(), numStudents, percentage + "%"));
	    }
	    
        studentTable.setItems(gradeData);
	}
	
	public void switchGraph() {
	    
	    if (showPie) {
	        //switch to lineChart
	        //barChart.setVisible(true);
	        bar_pane.setVisible(true);
	        pie_chart.setVisible(false);
	    }
	    
	    else {
	        //barChart.setVisible(false);
	        bar_pane.setVisible(false);
            pie_chart.setVisible(true);
	    }
	    
	    showPie = !showPie;
	}
	
	private void updatePieChart() {
	    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
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
	 * populates the combobox
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
		
		
		for (int i = 0; i < (int)Grader.getCurve().getGrade("D").value(); ++i) {
		    if (scores[i] > 0) {
		        XYChart.Data<Integer, String> data = new XYChart.Data<Integer, String>(scores[i], Integer.toString(i));
		        fSeries.getData().add(data);
		        maxVal = (maxVal < scores[i]) ? scores[i] : maxVal; 
		    }
		}
		for (int i = (int)Grader.getCurve().getGrade("D").value(); i < (int)Grader.getCurve().getGrade("C").value(); ++i) {
		    if (scores[i] > 0) {
		        XYChart.Data<Integer, String> data = new XYChart.Data<Integer, String>(scores[i], Integer.toString(i));
		        dSeries.getData().add(data);
		        maxVal = (maxVal < scores[i]) ? scores[i] : maxVal;
		    }
		}
		for (int i = (int)Grader.getCurve().getGrade("C").value(); i < (int)Grader.getCurve().getGrade("B").value(); ++i) {
		    if (scores[i] > 0) {
		        XYChart.Data<Integer, String> data = new XYChart.Data<Integer, String>(scores[i], Integer.toString(i));
		        cSeries.getData().add(data);
		        maxVal = (maxVal < scores[i]) ? scores[i] : maxVal;
		    }
		}
		for (int i = (int)Grader.getCurve().getGrade("B").value(); i < (int)Grader.getCurve().getGrade("A").value(); ++i) {
		    if (scores[i] > 0) {
		        XYChart.Data<Integer, String> data = new XYChart.Data<Integer, String>(scores[i], Integer.toString(i));
		        bSeries.getData().add(data);
		        maxVal = (maxVal < scores[i]) ? scores[i] : maxVal;
		    }
		}
		for (int i = (int)Grader.getCurve().getGrade("A").value(); i <= 100; ++i) {
		    if (scores[i] > 0) {
		        XYChart.Data<Integer, String> data = new XYChart.Data<Integer, String>(scores[i], Integer.toString(i));
		        aSeries.getData().add(data);
		        maxVal = (maxVal < scores[i]) ? scores[i] : maxVal;
		    }
		}

		barChart.getData().clear();
		barChart.getData().add(fSeries);
		barChart.getData().add(dSeries);
		barChart.getData().add(cSeries);
		barChart.getData().add(bSeries);
		barChart.getData().add(aSeries);
		xAxis.setUpperBound(maxVal);
	}

	/**
	 * Handles adding a new grade (bringing up the dialog)
	 */
	@FXML
	private void handleNewGrade(ActionEvent event) {
		Stage stage = new Stage();
		GraderPopup.setIcon(stage);
		try {
			Scene scene = new Scene((AnchorPane) FXMLLoader.load(getClass()
					.getClassLoader().getResource("view/graph/NewGrade.fxml")));
			stage.setScene(scene);
			newGradeButton.setDisable(true);
			stage.setOnHidden(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent arg0) {
					newGradeButton.setDisable(false);
				}
			});
			stage.show();
		} catch (IOException ex) {
			Debug.log("ERROR", "Could not load new grade popup");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Handles the selection of a new grade to edit in the dropdown menu
	 */
	@FXML
	private void selectNewGrade(ActionEvent event) {
		Grade grade = Grader.getCurve().getGrade(gradeSelectDropdown.getValue());
		if(grade != null) {
			slider.setValue(grade.value());
			slider.setMax(100);
			if (Grader.getCurve().getGradeAbove(grade) != null) {
				slider.setMax(Grader.getCurve().getGradeAbove(grade).value());
			}
			slider.setMin(0);
			if (Grader.getCurve().getGradeBelow(grade) != null) {
				slider.setMin(Grader.getCurve().getGradeBelow(grade).value());
			}
			slider.setValue(grade.value());
			slider.setVisible(true);
		}
	}
	
	private class StudentsPerGradeObject {
        private String grade;
        private int numStudents;
        private String percentage;
        
        public StudentsPerGradeObject(String newGrade, int newNumStudents, String newPercentage) {
            grade = newGrade;
            numStudents = newNumStudents;
            percentage = newPercentage;
        }
        
        public String getGrade() {
            return grade;
        }
        
        public int getNumStudents() {
            return numStudents;
        }
        
        public String getPercentage() {
            return percentage;
        }
    }

	/**
	 * Allows other classes to update the tab
	 */
	public static void refresh() {
		currentInstance.update();
	}
}
