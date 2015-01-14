package model.roster;

import javax.swing.JOptionPane;

import model.driver.Grader;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Controls the visual gradebook
 * 
 * @author Gavin Scott
 */
public class GradebookTable {
	@FXML
	private TableView<Student> mainTable;
	@FXML
	private TableColumn<Student, String> idCol;
	@FXML
	private TableColumn<Student, Double> totalGradeCol;
	@FXML
	private TableColumn<Student, String> nameCol;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	Button asgnButton;
	@FXML
	Button studentButton;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	protected void initialize() {
		mainTable.setEditable(false);

		nameCol.setMinWidth(100);
		nameCol.setEditable(false);
		nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>(
				"name"));

		idCol.setMinWidth(100);
		idCol.setEditable(false);
		idCol.setCellValueFactory(new PropertyValueFactory<Student, String>(
				"id"));

		totalGradeCol.setEditable(false);
		totalGradeCol.setMinWidth(100);
		totalGradeCol
				.setCellValueFactory(new PropertyValueFactory<Student, Double>(
						"totalScore"));

		for (GradedItem item : Grader.getRoster().getAssignments()) {
			final TableColumn<Student, Double> newColumn = new TableColumn<Student, Double>(
					item.name());
			newColumn.setMinWidth(100);
			newColumn.setEditable(true);

			newColumn.setCellValueFactory(new Callback() {
				public SimpleDoubleProperty call(
						CellDataFeatures<Student, Double> param) {
					return param.getValue().getAssignmentScoreAsProperty(
							newColumn.getText());
				}

				public Object call(Object param) {
					return call((CellDataFeatures<Student, Double>) (param));
				}
			});

			mainTable.getColumns().add(newColumn);
		}

		mainTable.setItems(Grader.getStudentList());
	}

	@FXML
	void asgnButton(ActionEvent e) {
		Stage newStage = new Stage();
		AddAssignmentDialog popup = new AddAssignmentDialog();
		popup.start(newStage);
		popup.setParent(this);
		asgnButton.setDisable(true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	void addAssignmentColumn(String asgn, String descr) {

		final TableColumn<Student, Double> newColumn = new TableColumn<Student, Double>(
				asgn);
		newColumn.setMinWidth(100);
		newColumn.setEditable(true);

		Grader.addAssignment(new GradedItem(asgn, descr));

		newColumn.setCellValueFactory(new Callback() {
			public SimpleDoubleProperty call(
					CellDataFeatures<Student, Double> param) {
				return param.getValue().getAssignmentScoreAsProperty(
						newColumn.getText());
			}

			public Object call(Object param) {
				return call((CellDataFeatures<Student, Double>) (param));
			}
		});
		mainTable.getColumns().add(newColumn);
	}

	@FXML
	void studentButton(ActionEvent e) {
		String name = JOptionPane.showInputDialog("Please input a name");
		Grader.addStudent(new Student(name, ""
				+ (name.length() * 236 - name.charAt(0))));
		mainTable.setItems(Grader.getStudentList());
	}
}
